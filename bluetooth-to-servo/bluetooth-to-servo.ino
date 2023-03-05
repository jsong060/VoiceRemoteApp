#include<ESP32Servo.h>

//BLUETOOTH
#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>



#define SERVICE_UUID "5309565c-0798-4951-af9b-2d2d171d2cff"
#define CHARACTERISTIC_UUID "5309565c-0798-4951-af9b-2d2d171d2cff"

//#define CHARACTERISTIC_UUID_RX "db7700c0-707a-4f2a-b2fa-02fa73a4d344"

//defining the server, service and characteristic globally
BLEServer *pServer;
BLEService *pService;
BLECharacteristic *pCharacteristic;

void getValues(String myStr,int & a, int & b);

//other global variables used
int tXValue = 0; // won't be used 
bool deviceConnected = false;
//const int readPin = 18; // Rx pin for serial communication
//const int LED = 2; // LED for observing transmission

std::string rxValue; // received command string

class MyServerCallBacks: public BLEServerCallbacks {
  void onConnect(BLEServer *pServer) {
    deviceConnected = true;
  };

  void onDisconnect(BLEServer *pServer) {
    deviceConnected = false;
  }
};

const int MOTORPIN[2] = {16,18};
Servo motor[2];

class MyCallBacks : public BLECharacteristicCallbacks {
  void onWrite(BLECharacteristic *pCharacteristic) {
    rxValue = pCharacteristic->getValue();

    if(rxValue.length() > 0) {
      Serial.println("Received value: ");

      for (int i = 0; i < rxValue.length(); i++) {
        Serial.print(rxValue[i]);
      }

      /*INSERT FUNCTIONALITY TO ACT UPON RECEIVED COMMAND (rxValue)*/
      String mySignal = String(rxValue.c_str());
      Serial.println("my string : " + mySignal);
      int num1 = 99; int num2 = 99;
      getValues(mySignal, num1, num2);

      Serial.print("Value 1 = ");
      Serial.println(num1);

      Serial.print("Value 2 = ");
      Serial.println(num2);

      motor[num1].attach(MOTORPIN[num1]);
      motor[num1].write(60);
      delay((num2 * 1000) + 30);
      motor[num1].write(0);
      delay(300);
      motor[num1].detach();
      Serial.println("\n****\n");
    }
  }
};

//---------------





void setup() {
  // put your setup code here, to run once:
  //Serial.begin(9600);
   Serial.begin(115200);

  //pinMode(LED, OUTPUT); 

  Serial.println("Starting BLE Server on ESP32!");

  BLEDevice::init("EPS32 BLE Server for Voice Controlled Remote");

  // Creating the BLE Server on the EPS32
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallBacks());


  // Creating the BLE service
  pService = pServer->createService(SERVICE_UUID);

  // Creating the BLE characteristics
  pCharacteristic = pService->createCharacteristic(
                                         CHARACTERISTIC_UUID,
                                         BLECharacteristic::PROPERTY_READ |
                                         BLECharacteristic::PROPERTY_WRITE
                                       );

  pCharacteristic->setCallbacks(new MyCallBacks());
  pCharacteristic->addDescriptor(new BLE2902());
  pCharacteristic->setValue("Hello World, Send me packets!");

  // start the device
  pService->start();
  // BLEAdvertising *pAdvertising = pServer->getAdvertising();  // this still is working for backward compatibility
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  pAdvertising->setMinPreferred(0x06);  // functions that help with iPhone connections issue
  pAdvertising->setMinPreferred(0x12);
  BLEDevice::startAdvertising();
  Serial.println("Characteristic defined!");
}



void loop() {
  
  if(deviceConnected) {
    Serial.print("The received string is: ");
    Serial.println(rxValue.c_str());
  } else {
    delay(500);
    pServer->startAdvertising();
    Serial.println("Started advertising");
  }
  
  motor[0].attach(MOTORPIN[0]);
  motor[1].attach(MOTORPIN[1]);

  motor[0].write(0);
  motor[1].write(0);

  delay(300);

  motor[0].detach();
  motor[1].detach();
  
  // put your main code here, to run repeatedly:
  while(1);  
}



void getValues(String myStr, int & a, int & b){
  char line[10]; 
  myStr.toCharArray(line, 6);
  char *token[2];
  char *delimiter = " ";

  token[0] = strtok(line, delimiter);
  delay(10);
  token[1] = strtok(NULL, delimiter);
  delay(10);

  a = atoi(token[0]);
  b = atoi(token[1]);
}
