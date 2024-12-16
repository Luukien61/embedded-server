import network
import time
import urequests as requests
import gc
import machine as Gpio

# Led_pin define
Blue_Led = Gpio.Pin(6, Gpio.Pin.OUT)

# Define the server URLs & auth_token
uplink_server_url = "http://console.thingzmate.com/api/v1/device-types/rpi/devices/arduino-testing/http-uplink"
downlink_server_url = "http://console.thingzmate.com/api/v1/device-types/rpi/devices/arduino-testing/http-downlink"
auth_token = "85a505d1273bf2432792b108c77ee601"

# Your Wi-Fi credentials
ssid = "Wokwi-GUEST"
password = ""

# Example data to send (for POST request)
data_to_send = {
    "sensor_id": 1,
    "value": 25.5
}

# Create a Wi-Fi station interface
wlan = network.WLAN(network.STA_IF)

# Activate the interface
wlan.active(True)

# Connect to the Wi-Fi network
wlan.connect(ssid, password)

# Wait for the connection to be established
while not wlan.isconnected():
    pass

# Print the network configuration
print('Connected to', ssid)
print('IP address:', wlan.ifconfig()[0])

# Define headers with the Authorization token
headers = {
    "Authorization": "Bearer " + auth_token
}

# Initialize the response variable as None
response = None

while True:
    try:
        # Perform a POST request to send data to the server
        response = requests.post(uplink_server_url, json=data_to_send, headers=headers)
        if response.status_code == 200:
            print("Data sent successfully!")
        else:
            print("Failed to send data. Status code:", response.status_code)
        response.close()  # Close the response to free up memory

        # Perform a GET request to retrieve data from the server
        response = requests.get(downlink_server_url, headers=headers)
        if response.status_code == 200:
            server_data = response.json()  # Assuming the server responds with JSON data
            print("Received data:", server_data)
            if server_data == 1:
                print("LED_ON")
                Blue_Led.value(1)
            elif server_data == 2:
                print("LED_OFF")
                Blue_Led.value(0)
        else:
            print("Failed to retrieve data. Status code:", response.status_code)
    except Exception as e:
        print("No_RX_DATA")
    finally:
        if response is not None:
            response.close()

    # Force garbage collection to free up memory
    gc.collect()

    time.sleep(30)  # Adjust the sleep interval as needed