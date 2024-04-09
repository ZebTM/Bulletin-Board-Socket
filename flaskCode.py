from flask import Flask, request, jsonify

app = Flask(__name__)

clients = []
messages = []

@app.route('/join', methods=['POST'])
def join():
    username = request.json.get('username')
    clients.append(username)
    notify_all(f'{username} joined the group.')
    return jsonify({'message': 'Joined successfully'})

@app.route('/leave', methods=['POST'])
def leave():
    username = request.json.get('username')
    if username in clients:
        clients.remove(username)
        notify_all(f'{username} left the group.')
        return jsonify({'message': 'Left successfully'})
    else:
        return jsonify({'error': 'User not found'})

@app.route('/post', methods=['POST'])
def post_message():
    sender = request.json.get('sender')
    subject = request.json.get('subject')
    content = request.json.get('content')
    message_id = len(messages) + 1
    message = {
        'id': message_id,
        'sender': sender,
        'subject': subject,
        'content': content
    }
    messages.append(message)
    notify_all(f'New message posted by {sender}: {subject}')
    return jsonify({'message': 'Posted successfully'})

@app.route('/messages/<int:message_id>', methods=['GET'])
def get_message(message_id):
    message = next((msg for msg in messages if msg['id'] == message_id), None)
    if message:
        return jsonify(message)
    else:
        return jsonify({'error': 'Message not found'})

@app.route('/users', methods=['GET'])
def get_users():
    return jsonify({'users': clients})

def notify_all(message):
    # Function to notify all clients of a new event, will need to write
    pass

if __name__ == '__main__':
    app.run()