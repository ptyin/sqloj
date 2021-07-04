#FROM mongo
#
## prerequisite
#RUN sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list && apt-get update \
#    && DEBIAN_FRONTEND=noninteractive apt-get -yq --no-install-recommends install nginx python3 python3-pip\
#    && DEBIAN_FRONTEND=noninteractive apt-get -yq install --fix-missing \
#    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
#
## app
#COPY /app/build/ /var/www/html/
## nginx
#COPY /conf/nginx.conf /etc/nginx/
## backend
#COPY /backend/ /opt/sqloj/
#
#WORKDIR /opt/sqloj
#RUN pip3 install -r /opt/sqloj/requirements.txt
#
#CMD service nginx start \
#    && mongod --syslog --fork \
#    && python3 wsgi.py
#
#EXPOSE 80
#VOLUME /var/lib/sqloj

FROM ptyin/sqloj:v2
COPY /backend/src/task.py /opt/sqloj/src/task.py
COPY /backend/src/module/judge.py /opt/sqloj/src/module/judge.py
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime