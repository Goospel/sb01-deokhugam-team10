#!/bin/bash
set -eux

# 로그 파일
LOG=/home/ubuntu/deploy.log
ERR_LOG=/home/ubuntu/deploy_err.log

echo "=== $(date '+%Y-%m-%d %H:%M:%S') DEPLOY START ===" >> $LOG

# 1) 빌드된 JAR 파일 찾기 (없으면 에러)
BUILD_DIR=/home/ubuntu/app/build/libs
BUILD_JAR=$(ls "$BUILD_DIR"/*.jar 2>/dev/null || true)
if [[ -z "$BUILD_JAR" ]]; then
  echo "### ERROR: 빌드된 JAR 파일을 찾을 수 없습니다: $BUILD_DIR/*.jar" >> $LOG
  exit 1
fi

JAR_NAME=$(basename "$BUILD_JAR")
echo ">>> 빌드 파일명: $JAR_NAME" >> $LOG

# 2) 배포 디렉토리에 복사
DEPLOY_PATH=/home/ubuntu/app/
echo ">>> build 파일 복사: $BUILD_JAR → $DEPLOY_PATH" >> $LOG
cp "$BUILD_JAR" "$DEPLOY_PATH"

# 3) 기존 애플리케이션 프로세스 종료
echo ">>> 실행 중인 애플리케이션 PID 확인" >> $LOG
PIDS=$(pgrep -f "java -jar $DEPLOY_PATH$JAR_NAME" || true)
if [[ -n "$PIDS" ]]; then
  echo ">>> killing PIDs: $PIDS" >> $LOG
  echo "$PIDS" | xargs kill -15
else
  echo ">>> 종료할 프로세스가 없습니다." >> $LOG
fi

# 4) 새 애플리케이션 실행
DEPLOY_JAR="$DEPLOY_PATH$JAR_NAME"
echo ">>> 배포할 JAR: $DEPLOY_JAR" >> $LOG
nohup java -jar "$DEPLOY_JAR" >> $LOG 2>> $ERR_LOG &
sleep 2

echo "=== $(date '+%Y-%m-%d %H:%M:%S') DEPLOY END ===" >> $LOG
exit 0
