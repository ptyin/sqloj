# SQLOJ
An open judge platform for SQL

## compile
    cd app
    npm install && npm run build

## run
    docker run -p 80:80 -v D:\SQLOJ\mongodb\data:/data/db -v D:\SQLOJ\mongodb\config:/data/configdb -v D:\SQLOJ\sqlite:/var/lib/sqloj --name test ptyin/sqloj:latest 
