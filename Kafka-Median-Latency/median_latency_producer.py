from kafka import KafkaProducer
import time
import struct
from datetime import datetime

with open("message.txt", "rb") as f:
    message_data = f.read()

producer = KafkaProducer(bootstrap_servers='localhost:9092')

topic = "test-topic"

for i in range(1, 10001):
    timestamp = time.time()
    timestamp_bytes = struct.pack('d', timestamp)
    full_message = timestamp_bytes + message_data
    producer.send(topic, value=full_message)

    if i % 1000 == 0:
        print(f"[{datetime.now().strftime('%H:%M:%S')}] Produced {i} messages...")

producer.flush()
producer.close()
print("Finished producing 10,000 messages.")
