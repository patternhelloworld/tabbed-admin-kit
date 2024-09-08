FROM node:18.20.4-slim AS build

ARG PROJECT_ROOT_IN_CONTAINER

RUN mkdir -p $PROJECT_ROOT_IN_CONTAINER
COPY . $PROJECT_ROOT_IN_CONTAINER
WORKDIR $PROJECT_ROOT_IN_CONTAINER
RUN export NODE_OPTIONS="--max-old-space-size=2048"
RUN whereis npm && alias npm='node --max_old_space_size=2048 /usr/local/bin/npm'
RUN export NODE_OPTIONS="--max-old-space-size=2048"
RUN if [ -d $PROJECT_ROOT_IN_CONTAINER/node_modules ]; then echo "[NOTICE] The node_modules folder exists. Skipping 'npm install'... "; else npm install --legacy-peer-deps; fi
RUN npm cache clean --force
RUN npm run build:prod

FROM nginx:stable

RUN apt-get update -qqy && apt-get -qqy --force-yes install curl runit wget unzip vim && \
    rm -rf /var/lib/apt/lists/* /var/cache/apt/*

ARG PROJECT_ROOT_IN_CONTAINER

COPY --chown=nginx --from=build $PROJECT_ROOT_IN_CONTAINER/dist/ $PROJECT_ROOT_IN_CONTAINER

USER root
WORKDIR $PROJECT_ROOT_IN_CONTAINER

COPY ./.docker/ssl /etc/nginx/ssl

COPY ./.docker/entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

ENTRYPOINT bash /entrypoint.sh

