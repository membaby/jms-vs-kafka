from kafka import KafkaConsumer
import struct
import time
import statistics
from datetime import datetime

consumer = KafkaConsumer(
    'test-topic',
    bootstrap_servers='localhost:9092',
    group_id='latency-group',
    auto_offset_reset='earliest'
)

latencies = []
max_messages = 10000
received = 0

for message in consumer:
    data = message.value
    sent_timestamp = struct.unpack('d', data[:8])[0]
    received_timestamp = time.time()
    latency = received_timestamp - sent_timestamp
    latencies.append(latency)
    received += 1

    if received % 1000 == 0:
        print(f"[{datetime.now().strftime('%H:%M:%S')}] Consumed {received} messages...")

    if received >= max_messages:
        break

consumer.close()

median_latency = statistics.median(latencies)
print(f"\nMedian Latency over {max_messages} messages: {median_latency * 1000:.2f} ms")
