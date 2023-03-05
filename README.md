# VoiceRemoteApp 

Android Studio + Arduino ESP32 based project for [Hack the Hill](https://tracker.hackthehill.com/resources) Hackathon event held in University of Ottawa between March 3-5, 2023, with the task of completing one or more of the challenges within 36 hrs. 

## Table of Contents
- [Participated Challenges](#challenges)
- [Project Task](#task)
- [Project Summary](#summary)
- [Scope](#scope)
  - [VoiceRemote App](#app)
  - [ESP32 BLE Server](#ble)
  - [ESP32 Steel-geared Servo System](#servo)
- [Team](#team)


## Challenges participated <a name = "challenges"></a>
- Maker Con Challenge Presented by Maker Jam
  - [Voice activated hospital chair remote](https://makerepo.com/project_proposals/350.makercon-voice-activated-chair-remote)

## Task <a name = "task"></a>

The project is tasked to provide a solution to the problem that many people with disabilities who use power recliner chairs or remote controlled hospital beds with reduced mobility cannot push the buttons on their chairs/beds' remote controls with physical power. The challenge was introduced by [TetraSociety](https://makerepo.com/project_proposals/350.makercon-voice-activated-chair-remote).


## Project Summary <a name = "summary"></a>

A design solution comprising of 5 cascading communicating components to achieve a voice operated, software+hardware driven mechanical system was layed out:

- Command based voice recognition
- Text filtering/parsing and generating data packets containing control signals 
- Android Bluetooth Low Energy (BLE) client/peripheral system 
- Arduino ESP32 Bluetooth Low Energy (BLE) server/center system
- Mechanical servo system controlled by Arduino ESP32 

## Scope of Capability <a name = "scope"></a>

### VoiceRemote App <a name = "app"></a>

The Android app is capable of selectively receiving voice commands, with optional duration parameter passing through voice as well. The speech is transcribed and filtered, then converted to serially transmittable string packets to be sent through the peripheral output of the Android system's [BLE](https://developer.android.com/guide/topics/connectivity/bluetooth/ble-overview) functionality. 

Speech is transcribed in real-time in determined short time intervals using Android's [SpeechRecognizer](https://developer.android.com/reference/android/speech/SpeechRecognizer) library. Provides the user to prompt a programmable button command, e.g. "up two" for pressing the up button for two seconds, or just "down" for pressing the down button for a programmed default interval. 

### Arduino ESP32 Bluetooth Low Energy (BLE) Server System <a name = "ble"></a>

The ESP32 board is programmed to host a [BLE](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/api-reference/bluetooth/index.html) server to scan for serial peripheral input at a baud rate of 115200 bps. The serial input is converted into control signals for the servos to control button interaction. 

### Arduino ESP32 Steel-geared Servo System <a name = "servo"></a>

The ESP32 is also programmed to get the serial control input and translate that into a set of servo control signals for button interaction. The placement of the buttons can be customizable for possible future implementations, allowing up to multiple button interactions and different time intervals, potentially allowing generic application for various remote control devices. 



## Team Members <a name = "team"></a>
- [/jsong060](https://github.com/jsong060) 
- [/Jelno029](https://github.com/Jelno029)
- [/algorhtym](https://github.com/algorhtym)





