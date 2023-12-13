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