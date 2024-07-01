#include <ESP8266WiFi.h>                  // ESP8266 Wi-Fi kütüphanesini ekliyoruz.
#include <WiFiClientSecure.h>             // Güvenli Wi-Fi istemcisi kütüphanesini ekliyoruz.
#include <UniversalTelegramBot.h>         // Telegram botu için evrensel kütüphaneyi ekliyoruz.
#include <ArduinoJson.h>                  // JSON işlemleri için Arduino JSON kütüphanesini ekliyoruz.

const char* ssid = "Redmi_10";            // Wi-Fi ağ adı (SSID)
const char* password = "eren1111";        // Wi-Fi şifresi

const char* telegramBotToken = "6849422209:AAFeQMXd2At8mB0iC7dw6R9q1NfZ7TP4T8E"; // Telegram Bot Token
const char* chat_id = "6079782907";       // Telegram sohbet kimliği (chat ID)

WiFiClientSecure client;                  // Güvenli Wi-Fi istemcisi oluşturuluyor.
UniversalTelegramBot bot(telegramBotToken, client); // Telegram botu oluşturuluyor.

const int trigPin = 9;                    // Ultrasonik sensörün trig pin numarası
const int echoPin = 10;                   // Ultrasonik sensörün echo pin numarası
long duration;                            // Ses dalgasının yolculuk süresi
int distance;                             // Ölçülen mesafe

void setup() {
  Serial.begin(115200);                   // Seri haberleşmeyi 115200 baud hızında başlatıyoruz.
  delay(10);                              // 10 milisaniye bekliyoruz.

  pinMode(trigPin, OUTPUT);               // trigPin çıkış olarak ayarlanıyor.
  pinMode(echoPin, INPUT);                // echoPin giriş olarak ayarlanıyor.

  // Wi-Fi'ye bağlanma
  WiFi.mode(WIFI_STA);                    // Wi-Fi'yi istasyon moduna (client) ayarlıyoruz.
  WiFi.begin(ssid, password);             // Wi-Fi ağına bağlanmaya başlıyoruz.
  while (WiFi.status() != WL_CONNECTED) { // Wi-Fi bağlantısı kurulana kadar bekliyoruz.
    delay(1000);                          // 1 saniye bekliyoruz.
    Serial.println("Wi-Fi'ye bağlanılıyor..."); // Bağlantı denemesi yapıldığını seri monitöre yazdırıyoruz.
  }
  Serial.println("Wi-Fi'ye bağlandı");    // Wi-Fi'ye bağlanıldığını seri monitöre yazdırıyoruz.

  client.setInsecure();                   // Güvenlik sertifikalarını kontrol etmeyi kapatıyoruz.
}

void loop() {
  // trigPin'i LOW yaparak temizliyoruz.
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);

  // trigPin'i 10 mikrosaniye boyunca HIGH yapıyoruz.
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // echoPin'i okuyoruz, geri dönen ses dalgasının süresini mikrosaniye cinsinden alıyoruz.
  duration = pulseIn(echoPin, HIGH);

  // Mesafeyi hesaplıyoruz.
  distance = duration * 0.034 / 2;

  // Mesafe belirli bir eşiğin altındaysa (örneğin 50 cm)
  if (distance < 50) {
    Serial.println("Hareket algılandı!"); // Seri monitöre hareket algılandığını yazdırıyoruz.
    bot.sendMessage(chat_id, "Hırsız var!", ""); // Telegram mesajı gönderiyoruz.
    delay(5000); // Tekrar kontrol etmeden önce 5 saniye bekliyoruz.
  }

  delay(500); // Bir sonraki ölçümden önce yarım saniye bekliyoruz.
}