# Demo Spring Boot Application for CSIPayJS

## Overview

CSIPayJS is client-side JavaScript library that can be used with eCommerce merchant web applications
to accept online payments from customers without the PCI compliance requirements. This implementation of a Java Spring Boot application that is common
among many eCommerce websites and shows how to integrate CSIPayJS.

More information about CSIPayJS can be found in the following guide:
https://csipay-pyxis.readme.io/reference/getting-started-1

## Prerequisites

- Java version 17
- Suitable version of Maven for building and running the application
- This application uses Thymeleaf for the server-side rendering of HTML pages: https://www.thymeleaf.org/

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

## Details of CSIPayJS Integration

The integration of CSIPayJS library involves HTML/JavaScript frontend
changes as well a backend changes in your Spring Boot application. The following steps
outline how these changes are implemented in this sample application. 

Please review the contents of the `application.properties` file in the `src/main/resources`
directory. This file contains the following properties:
- `csipay.url` - This is the Pyxis API URL from Constellation Payments Platform
- `security.token` - This is the identity and authorization token with the necessary permissions to use Pyxis API.
### 1. Home Page of the eCommerce site

_HomeController.java_ 

This is a Spring MVC controller class to deliver the home page of the sample eCommerce
merchant page. This simply returns an index.html file that is the sample 
product page of the online plant shop.

### 2. _index.html_ page

This is the home page of the online plant shop. The most important code to review
in this page are the  two JavaScript functions: `updateTotal()` and `pay()`.
The `updateTotal()` is a simple Javascript code to capture the events of the
shopper adding items to their shopping cart and calculating the total amount
before checkout. The `pay()` is the JavaScript code that POSTs to the
`CheckoutController` that renders the payment page powered by CSIPayJS.

### 3. Rendering the payment form with CSIPayJS

The HTML code required for rendering the CSIPayJS powered payment page is in the `pay.html` file.
The loading of this page is controlled by the Sprint Boot backend controller
`CheckoutController.java.`

The business logic coded in this controller is as follows:

First create an Order for the total sale amount coming in as the request parameter `amountStr`.
This class makes a POST request to the endpoint:
```agsl
https://test-pyxisapi.csipay.com/PyxisMasterApi/api/orders
```

The response to this call will fetch two important
data that is required to be passed on your payment page. The first one is the
`accessToken` which is required to securely initialize the CSIPayJS component
in the payment form (`pay.html` in this sample application). The second one is the
`orderID` which represents the payment intent of the eCommerce customer in
fulfilling their shopping order.

The controller is setting three model attributes that are required in the payment page
for completing the payment: `accessToken`, `orderId`, and `amount`. These are
set as session attributes that Thymeleaf can interpret and render appropriately
on the payment form page next.

### 4. Building the payment page and form

Please start with the `pay.html` page included in this project.
At the top of the page you will see the following JavaScript link included 
that is essential to initialize your payment page to work with CSIPayJS in a 
way that does removes your PCI compliance overhead.
```
<script src="https://test-pyxisapi.csipay.com/js/v1/csi_pay.js"></script>
 ```

Notice that, in the body of the `pay.html` page you have a HTML snippet:

```<p style="text-align: center"> Your Order total is: <strong th:text="${amount}"></strong></p>```

This is about informing your eCommerce customer about their payment amount
that is coming through the order they just placed. The syntax of how you capture 
from the model attributes we set in the Spring Controller backend should be
fairly self-explanatory. For more information on this, please refer to the Thymeleaf templating engine documentation. 
```https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html```

The most important part of building the payment page with the help of CSIPayJS
is to embed a few lines of JavaScript code into your HTML page. Take a look at the
the following JavaScript code snippet:
```<script th:inline="javascript">

    const csipay = CSIPayJS([[${accesstoken}]]);
    const components = csipay.components({orderId: [[${orderid}]]});

    components.addComponent('cardElement', 'full-card', {
        billingAddress: {
            type: "full"
        },
        style: {
            default: {
                backgroundColor: "white",
                padding: "3px"
            },
            error: {
                backgroundColor: "blue",
                border: "2px dashed deeppink",
                borderRadius: "10px"
            }
        }
    });
    
```
The above JavaScript code will render the required payment fields inside the
following `<div>` element you will see inside the `<form>` element

```
<div id="cardElement">
            <!-- our credit card UI component will be attached here -->
</div>
```
Please note that the `accessToken` and `orderId` are carried through from the backend
as model attributes and rendered in the HTML page with the rendering
capabilities of Thymeleaf. Please note that, we have done some minimal styling
for the look and feel of the payment page leaving out the option to customize by
the merchants as they see fit for their business.

### 5. Completing the payment

To complete the payment, the JavaScript below that is present in the `pay.html`
page is invoked:

```agsl
document.getElementById("myForm").addEventListener('submit', (event) => {
        event.preventDefault();

        const form = event.target.closest("form");
        csipay.payOrder()
            .then(payment_id => {
                // add the payment id to your form
                document.getElementById('payment_id').value = payment_id
                // submit your form to your backend
                form.submit()
            })
            .catch(err => console.error(err));
    });
```
Please note that the above JavaScript code is also populating the value for thw
following hidden input element in the form which is required for any
post-payment flows such as generating a confirmation page.

### 6. Generating confirmation page

CSIPayJS generates Javascript events such as `payment-completed` and `payment-failed`
for the application to handle and communicate to the user the state of the
payment. For example, as shown in the JavaScript code below, the payment-completed
event handler can call an endpoint to show a confirmation page. In this sample application
we use the same Spring Boot controller class, `CheckoutController` with a
new method mapping for `/confirm` in the URI with the `paymentID` as a request parameter.