#!/usr/bin/bash

sudo podman build -t quay.io/yigald/dev -f Dockerfile
sudo podman push --creds=yigald+yigald_robo:2XEC0Q7L8X0YP9LYZCE4BKAYIBGOZ8AQ6SFVYX06R7XKULPEZM20354U89QNXEMI quay.io/yigald/dev