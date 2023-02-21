import psycopg2
from flask import Flask, request, jsonify, send_file
from dotenv import load_dotenv

config = load_dotenv(".env")

app = Flask(__name__)

try:
    conn = psycopg2.connect(
            host=config.HOST,
            port=config.PORT,
            database=config.DATABASE,
            user=config.USER,
            password=config.PASSWORD
        )
except Exception as e:
    print(e)


@app.route('/profilePic', methods=['POST'])
def set_profile_pic():
    global conn
    if 'pic.png' not in request.files:
        return jsonify({'success': False, 'error': 'No file uploaded'}), 400

    file = request.files['pic.png']

    if file.filename == '':
        return jsonify({'success': False, 'error': 'No file selected'}), 400

    if file and allowed_file(file.filename):
    
        cur = conn.cursor()

        file_data = file.read()
        cur.execute(
            "INSERT INTO table_name (filename, file_data) VALUES (%s, %s)", ('pic.png', file_data))
        conn.commit()
        cur.close()
        conn.close()

        return jsonify({'success': True, 'error': ''}), 200
    else:
        return jsonify({'success': False, 'error': 'Invalid file type'}), 400


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in {'png', 'jpg', 'jpeg'}


@app.route('/profilePic', methods=['GET'])
def get_profile_pic():
    global conn
    conn = None
    cur = None
    try:
        cur = conn.cursor()

        cur.execute(
            "SELECT file_data FROM table_name WHERE filename = %s", ('pic.png',))
        data = cur.fetchone()

        if data:
            return send_file(data[0], attachment_filename='pic.png', as_attachment=True)
        else:
            return jsonify({'success': False, 'error': 'File not found'}), 404
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 500
    finally:
        if cur is not None:
            cur.close()
        if conn is not None:
            conn.close()


if __name__ == '__main__':
    app.run(host=config.HOST, port=4567)
