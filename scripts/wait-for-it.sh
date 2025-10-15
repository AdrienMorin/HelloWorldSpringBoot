#!/usr/bin/env bash
# scripts/wait-for-it.sh

# wait-for-it.sh: Wait for a service to be ready before executing a command

set -e

host="$1"
shift
port="$1"
shift
cmd="$@"

timeout=60
waited=0

until nc -z "$host" "$port" || [ $waited -eq $timeout ]; do
  echo "Waiting for $host:$port... ($waited/$timeout)"
  sleep 1
  waited=$((waited + 1))
done

if [ $waited -eq $timeout ]; then
  echo "Timeout waiting for $host:$port"
  exit 1
fi

echo "$host:$port is available"
exec $cmd
