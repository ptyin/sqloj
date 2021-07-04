# SQLOJ
An open judge platform for SQL

## RUN in Docker Container

### Compilation
    cd app && npm install && npm run build

### run
    docker pull ptyin/sqloj
    docker run -p 80:80 -v D:\SQLOJ\mongodb\data:/data/db -v D:\SQLOJ\mongodb\config:/data/configdb -v D:\SQLOJ\sqlite:/var/lib/sqloj --name test ptyin/sqloj:latest 

## For developer

### Install dependencies of front end
    cd app && npm install
### Start up reverse proxy server
    npm run start80

### Install dependencies of back end
    cd backend && pip install -r requirements.txt
After you have installed the dependencies, you still should install and run **MongoDB** on port 27017.

Then you are one step away to run the server.

### Start up server
    cd backend && python wsgi.py