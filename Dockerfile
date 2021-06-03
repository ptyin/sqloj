FROM nginx
ADD app/build /usr/share/nginx/html/
ADD conf/nginx.conf /etc/nginx/
EXPOSE 80

#FROM node
#ADD app/ /home/node/app/
#WORKDIR /home/node/app
#RUN npm install
#CMD npm run start
#EXPOSE 3000