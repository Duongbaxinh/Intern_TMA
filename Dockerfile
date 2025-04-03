FROM ubuntu:latest
LABEL authors="duong"

ENTRYPOINT ["top", "-b"]