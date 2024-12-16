#include <Wire.h>
#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <uri/UriBraces.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <ArduinoJson.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

#define SCREEN_WIDTH 128 // OLED width,  in pixels
#define SCREEN_HEIGHT 64 // OLED height, in pixels

// create an OLED display object connected to I2C
Adafruit_SSD1306 oled(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

#define WIFI_SSID "Wokwi-GUEST"
#define WIFI_PASSWORD ""
// Defining the WiFi channel speeds up the connection:
#define WIFI_CHANNEL 6

#define DHTPIN 14     // Chân kết nối cảm biến DHT22
#define DHTTYPE DHT22 // Loại cảm biến DHT

DHT dht(DHTPIN, DHTTYPE);

float temperature = 0.0;
float humidity = 0.0;

WebServer server(80);

const int LED1 = 26;
const int LED2 = 27;

bool led1State = false;
bool led2State = false;

int x, minX;

String userMessage=" ";

void sendHtml()
{
  String response = R"(
    <!DOCTYPE html>
    <html>
    <head>
      <title>ESP32 Web Server Demo</title>
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <style>
        * {
          margin: 0;
          padding: 0;
          box-sizing: border-box;
        }

        body {
          display: flex;
          justify-content: flex-start;
          align-items: center;
          height: 100vh;
          font-family: Arial, sans-serif;
          background-color: #f4f4f9;
        }

        .container {
          width: 50%;
          margin: auto;
          text-align: left;
          padding: 20px;
          background-color: #fff;
          border-radius: 10px;
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        h1 {
          font-size: 2em;
          margin-bottom: 20px;
          color: #333;
        }

        .button-group {
          display: flex;
          gap: 10px;
          margin-bottom: 20px;
        }

        .btn {
          background-color: #5B5;
          border: none;
          color: white;
          padding: 10px 20px;
          font-size: 1em;
          border-radius: 5px;
          cursor: pointer;
          text-decoration: none;
          transition: background-color 0.3s;
        }

        .btn:hover {
          background-color: #4a4;
        }

        .btn.OFF {
          background-color: #333;
        }

        p {
          font-size: 1em;
          color: #555;
          line-height: 1.5;
        }
      </style>


    </head>
    <body>
    <div class="container">
      <h1>ESP32 Web Server</h1>
      <div class="button-group">
        <a id="button1" href="/toggle/1" class="btn LED1_TEXT">LED 1</a>
        <a id="button2" href="/toggle/2" class="btn LED2_TEXT">LED 2</a>
      </div>
      <p>Temperature: <span id="temperature">T_TEMPERATURE</span></p>
      <p>Humidity: <span id="humidity">T_HUMIDITY</span></p>
    </div>
    <script>
      function fetchData() {
        fetch('/data')
          .then(response => response.json())
          .then(data => {
            document.getElementById('temperature').innerText = data.temperature + ' °C'
            document.getElementById('humidity').innerText = data.humidity + ' %'
            if(data.button1){
              document.getElementById("button1").classList.remove("OFF")
            }else{
              document.getElementById("button1").classList.add("OFF")
            }
            if(data.button2){
              document.getElementById("button2").classList.remove("OFF")
            }else{
              document.getElementById("button2").classList.add("OFF")
            }
          })
          .catch(error => console.error('Error fetching data:', error))
      }
      setInterval(fetchData, 1000)
    </script>
    </body>
    </html>

  )";
  response.replace("LED1_TEXT", led1State ? "ON" : "OFF");
  response.replace("LED2_TEXT", led2State ? "ON" : "OFF");
  response.replace("T_TEMPERATURE", String(temperature));
  response.replace("T_HUMIDITY", String(humidity));
  server.send(200, "text/html", response);
}



void setMessage(String userMessage){


    oled.clearDisplay(); // clear display
    oled.setTextSize(2);         // set text size
    oled.setTextColor(WHITE);
    oled.setTextWrap(false);
    oled.setCursor(x, 10);       // set position to display (x,y)
    oled.println(userMessage); // set text
    oled.display();
    x=x-3;
    if(x < minX) x= oled.width();

}

void setup(void)
{
  Serial.begin(115200);
  dht.begin();
  oled.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  oled.setTextWrap(false);
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD, WIFI_CHANNEL);
  Serial.print("Connecting to WiFi ");
  Serial.print(WIFI_SSID);
  // Wait for connection
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(100);
    Serial.print(".");
  }
  Serial.println(" Connected!");

  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  server.on("/", sendHtml);

  server.on("/data", []()
   {
     StaticJsonDocument<200> doc;
        doc["temperature"] = temperature;
        doc["humidity"] = humidity;
        doc["button1"] = led1State;
        doc["button2"] = led2State;
        String json;
        serializeJson(doc, json);
        server.send(200, "application/json", json);
    }
    );

  server.on(UriBraces("/message/{}"), HTTP_POST, []()
  {
      String userId = server.pathArg(0);

      if (server.hasArg("plain")) {
          String body = server.arg("plain");

          // Phân tích cú pháp JSON
          StaticJsonDocument<200> doc;
          DeserializationError error = deserializeJson(doc, body);

          if (error) {
              Serial.print("deserializeJson() failed: ");
              Serial.println(error.c_str());
              server.send(400, "application/json", "{\"error\": \"Invalid JSON\"}");
              return;
          }

          // Lấy các trường trong JSON
          String message = doc["message"].as<String>();
          Serial.print("Received message from user ");
          Serial.print(userId);
          Serial.print(": ");
          Serial.println(message);
          userMessage= message;

          x = oled.width();
          minX = -12 * strlen(userMessage.c_str());

          server.send(200, "application/json", "{\"status\": \"success\"}");

      } else {
          server.send(400, "application/json", "{\"error\": \"No body provided\"}");
      }
  });


  server.on(UriBraces("/toggle/{}"), []()
            {
    String led = server.pathArg(0);
    Serial.print("Toggle LED #");
    Serial.println(led);

    switch (led.toInt()) {
      case 1:
        led1State = !led1State;
        digitalWrite(LED1, led1State);
        break;
      case 2:
        led2State = !led2State;
        digitalWrite(LED2, led2State);
        break;
    }

    sendHtml(); });

  server.on("/button", ()[]
    {
        StaticJsonDocument<200> doc;
        doc["temperature"] = temperature;
        doc["humidity"] = humidity;
        doc["button1"] = led1State;
        doc["button2"] = led2State;
        String json;
        serializeJson(doc, json);
        server.send(200, "application/json", json);
    }
  )

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void)
{
  temperature = dht.readTemperature();
  humidity = dht.readHumidity();
  server.handleClient();
  setMessage(userMessage);
  delay(2);
}
