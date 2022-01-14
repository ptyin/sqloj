from werkzeug.serving import run_simple

import src

application = src.create_app()

if __name__ == "__main__":
    run_simple('0.0.0.0', 5366, application, use_reloader=True, use_debugger=True)
