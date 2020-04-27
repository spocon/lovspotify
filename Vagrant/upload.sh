#!/bin/bash

export UPLOAD=true
GPG_TTY=$(tty)
export GPG_TTY

ansible-playbook ../Ansible/start.yml -e lovspotify_version=1.0.3


