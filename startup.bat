xcopy /E/Y app\build nginx\html
cd nginx
nginx -s quit
start nginx
cd ..\backend
.\venv\Scripts\python.exe wsgi.py