#!/bin/bash
set -e

log_info()    { echo "[INFO] $1"; }
log_success() { echo "[DONE] $1"; }
log_error()   { echo "[ERROR] $1"; exit 1; }

## 옵션 기본값
# Application 위치
BACKEND_ROOT="/root/service"
# 앱 이름(디렉토리명)
APP_NAME="a2p-service"
# 빌드(jar) 파일위치(앱 디렉토리 위치 기준)
JAR_FILE="./build/libs/${APP_NAME}-0.0.1-SNAPSHOT.jar"
# 서비스 구동 포트명
PORT=9082

## 옵션 기본값
# 브랜치명
BRANCH="main"
# 환경변수파일 suffix
PROFILE="main"
# GIT PULL 실행
PULL=false
# DB flyway migration 구동 여부
DB=true
# DB flyway repair 구동 여부
REPAIR=false

## 인자 파싱
for arg in "$@"
do
  case $arg in
    branch=*)  BRANCH="${arg#branch=}" ;;
    pull=true) PULL=true ;;
    pull=false) PULL=false ;;
    db=true) DB=true ;;
    db=false) DB=false ;;
    repair=true) REPAIR=true ;;
  esac
done

if [[ "$BRANCH" == release/* ]]; then
  PROFILE="staging"
fi

log_info "PROFILE=$PROFILE"
log_info "소스 디렉토리($BACKEND_ROOT/$APP_NAME)로 이동"
cd "$BACKEND_ROOT/$APP_NAME" || log_error "디렉토리 이동 실패"

## Git pull
if [ "$PULL" = true ]; then
  log_info "Git에서 코드 최신화 중(브랜치=$BRANCH)..."
  git fetch origin $BRANCH || log_error "Git fetch 실패"
  git reset --hard "origin/$BRANCH" || log_error "Git reset 실패"
  git clean -fd || log_error "Git clean 실패"
    log_success "완료"
  fi

  ## Gradle 빌드
  log_info "빌드 중..."
  bash ./gradlew build -x test || log_error "빌드 중 실패"
  log_success "완료"


  ## 기존 프로세스 종료
  PID=$(lsof -t -i:$PORT || true)
  if [ -n "$PID" ]; then
    log_info "기존 프로세스 가동중이어서 종료"
    kill $PID || true
  fi

  EXTRA_ARGS=""

  ## Flyway enable 여부 결정
  if [[ "$BRANCH" == release/* ]]; then
    if [ "$DB" = true ]; then
      log_info "Flyway 인자 추가"
      EXTRA_ARGS="--spring.flyway.enabled=true"

      if [ "$REPAIR" = true ]; then
        log_info "Flyway Repair 인자 추가"
        EXTRA_ARGS="$EXTRA_ARGS --spring.flyway.repair=true"
      fi
    else
      EXTRA_ARGS="--spring.flyway.enabled=false"
    fi
  fi

  sleep 2

  LOG_PATH="$BACKEND_ROOT/app.log"
  > $LOG_PATH

  ## 애플리케이션 실행
  log_info "Application 빌드 생성중..."
  nohup java \
  -jar "$JAR_FILE" \
    --spring.profiles.active=$PROFILE \
    $EXTRA_ARGS \
    > $LOG_PATH 2>&1 &
  log_success "완료"

  APP_PID=$!
  sleep 2

  exit 0;
  # 헬스체크 루프
  log_info "서버 구동 체크중..."
  for i in {1..30}; do
if curl -fs http://localhost:$PORT/actuator/health 2>/dev/null | grep -q '"status":"UP"' > /dev/null; then
    log_info "체크 완료"
    log_success "✅ 최종 배포 완료!(pid=$APP_PID)"
    exit 0
  fi

  if grep -E -q "Application run failed|APPLICATION FAILED TO START" $LOG_PATH 2>/dev/null; then
    break
  fi

  sleep 2
done

echo "❌ 구동 실패.(로그 경로=$LOG_PATH)"
tail -n 10 $LOG_PATH
exit 1

