# Demo Spring Boot Application for ExactJS

## Overview

ExactJS is cliebt-side JavaScript library that can be used with eCommerce merchant web applications
to accept online payments from customers without the PCI compliance requirements. This is 
an implementation of a Java Spring Boot application that is common
among many eCommerce websites on how to integrate ExactJS.

More information about ExactJS can be found in the following guide:
https://developer.exactpay.com/docs/Payment-Forms

## Prerequisites

- Java version 17
- Suitable version of Maven for building and running the application

## Steps to use the demo app
- Clone the repository
```agsl
git clone https://github.com/exact-payments/merchant-demo-exactjs-spring-boot.git
```

- Move to the directory of the source code of the application
```agsl
cd merchant-demo-exactjs-spring-boot
```
- Build and run the application
```agsl
./mvnw spring-boot:run
```

- Open the browser and load the following URL
```agsl
http://localhost:8080
```
You will see the home page of a eCommerce website. Add the products to the
shopping cart and checkout.

## Details of ExactJS Integration

The integration of ExactJS library involves HTML/JavaScript frontend
changes as well a backend changes in your Spring Boot application. The following steps
outline how these changes are implemented in this sample application.

### 1. Home Page of the eCommerce site

HomeController.java 

This is a Spring MVC controller class to deliver the home page of the sample eCommerce
merchant page.

CheckoutController.java

This is the Spring MVC controller class that loads the pay.html page. 

