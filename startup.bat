xcopy /E/Y app\build nginx\html
cd nginx
start nginx
cd ..\backend
.\venv\Scripts\python.exe wsgi.py