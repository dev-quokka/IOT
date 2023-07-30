#include <ESP8266WiFi.h>
  
const char *ssid = "glory";//개인 와이파이 이름 입력 
const char *password = "**비밀번호**";//개인 와이파이 비밀번호 입력
  
void WiFiEvent(WiFiEvent_t event)
{
    Serial.printf("[WiFi-event] event: %d\n", event);
  
    switch(event)
    {
        case WIFI_EVENT_STAMODE_GOT_IP:
            Serial.println("WiFi connected");
            Serial.print("IP address: ");
            Serial.println(WiFi.localIP());
            break;
        case WIFI_EVENT_STAMODE_DISCONNECTED:
            Serial.println("WiFi lost connection");
            break;
    }
}
  
void setup()
{
    Serial.begin(115200);//보드레이트 속도
  
    WiFi.disconnect(true);
  
    delay(1000);
  
    WiFi.onEvent(WiFiEvent);
  
    WiFi.begin(ssid, password);
  
    Serial.println();
    Serial.println();
    Serial.println("Wait for WiFi... ");
}
  
  
void loop()
{
    delay(1000);
}