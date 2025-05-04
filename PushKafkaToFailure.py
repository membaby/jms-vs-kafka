import time
from kafka import KafkaProducer

# Configuration
bootstrap_servers = ['localhost:9092']
topic = 'test-topic'
num_messages = 10
message_size = 1024  # 1 KB
target_throughput = 10000  # msgs/sec

interval_sec = 1.0 / target_throughput
sleep_sec = interval_sec - 0.2 * interval_sec

# Create payload (1 KB of 'A's)
payload = b'A' * message_size

# Set up Kafka producer
producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    acks='1',
    key_serializer=None,
    value_serializer=None
)

start_time = time.time()

for _ in range(num_messages):
    print(f"Sending message {_ + 1} of {num_messages}")
    producer.send(topic, value=payload)
    time.sleep(sleep_sec)

print('Fff')
end_time = time.time()
print('Fff')

elapsed = end_time - start_time
actual_throughput = num_messages / elapsed

print(f"Sent {num_messages} messages in {elapsed:.2f} seconds")
print(f"Actual throughput: {actual_throughput:.2f} messages/sec")
