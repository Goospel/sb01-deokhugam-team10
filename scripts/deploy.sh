#!/bin/bash
set -eux

LOG=/home/ubuntu/deploy.log
ERR_LOG=/home/ubuntu/deploy_err.log
DEPLOY_PATH=/home/ubuntu/app/

echo "=== $(date '+%Y-%m-%d %H:%M:%S') DEPLOY START ===" >> $LOG

# 1) plain.jar 제외한 첫 번째 일반 JAR 선택
BUILD_JAR=$(
  ls "$DEPLOY_PATH"/build/libs/*.jar \
    2>/dev/null \
  | grep -v "plain.jar" \
  | head -n1
)
if [[ -z "$BUILD_JAR" ]]; then
  echo "### ERROR: plain 제외한 JAR 파일을 찾을 수 없습니다!" >> $LOG
  exit 1
fi

JAR_NAME=$(basename "$BUILD_JAR")
echo ">>> 사용할 JAR: $JAR_NAME" >> $LOG

# 2) 복사
echo ">>> 복사: $BUILD_JAR → $DEPLOY_PATH" >> $LOG
cp "$BUILD_JAR" "$DEPLOY_PATH"

# 3) 기존 프로세스 종료
echo ">>> 기존 애플리케이션 PID 확인" >> $LOG
PIDS=$(pgrep -f "java -jar $DEPLOY_PATH$JAR_NAME" || true)
if [[ -n "$PIDS" ]]; then
  echo ">>> Killing PIDs: $PIDS" >> $LOG
  echo "$PIDS" | xargs kill -15
else
  echo ">>> 종료할 프로세스 없음" >> $LOG
fi

# 4) 새 애플리케이션 실행 (prod 프로파일 활성화)
echo ">>> 새 애플리케이션 실행: $JAR_NAME (prod 프로파일)" >> $LOG
nohup java -jar "$DEPLOY_PATH$JAR_NAME" \
  --spring.profiles.active=prod \
  >> $LOG 2>> $ERR_LOG &

sleep 2
echo "=== $(date '+%Y-%m-%d %H:%M:%S') DEPLOY END ===" >> $LOG
exit 0
