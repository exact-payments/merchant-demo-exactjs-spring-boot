// This is a simple test file for the updateTotal function
// To run these tests, you would need a JavaScript testing framework like Jest

// Mock DOM elements
document.body.innerHTML = `
  <div id="amount">Your Order Total: $0.00</div>
  <div id="item1">$1.50</div>
  <div id="item2">$2.75</div>
  <div id="item3">$10.20</div>
  <div id="item4">$0.20</div>
`;

// Mock the updateTotal function
let total = 0;
function updateTotal(clicked) {
    var amountElement = document.getElementById("amount");
    var amount = amountElement.textContent || amountElement.innerText;
    var index = amount.indexOf("$");
    var amountStr = amount.substring(index+1);
    var priceStr = '';
    let itemNumber = clicked.substring(6);
    var priceElement = document.getElementById("item"+itemNumber);
    priceStr = priceElement.textContent || priceElement.innerText;
    var price = priceStr.substring(1);
    total += parseFloat(price);
    amountElement.innerHTML = "Your Order Total: $" + total.toFixed(2);
}

// Test cases
describe('updateTotal function', () => {
  beforeEach(() => {
    // Reset total and amount display before each test
    total = 0;
    document.getElementById('amount').innerHTML = "Your Order Total: $0.00";
  });

  test('should add $1.50 to the total', () => {
    updateTotal('button1');
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $1.50");
  });

  test('should add $2.75 to the total', () => {
    updateTotal('button2');
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $2.75");
  });

  test('should add $10.20 to the total', () => {
    updateTotal('button3');
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $10.20");
  });

  test('should add $0.20 to the total', () => {
    updateTotal('button4');
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $0.20");
  });

  test('should correctly add multiple items with decimal prices', () => {
    updateTotal('button1'); // $1.50
    updateTotal('button4'); // $0.20
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $1.70");
    
    updateTotal('button2'); // $2.75
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $4.45");
    
    updateTotal('button3'); // $10.20
    expect(document.getElementById('amount').innerHTML).toBe("Your Order Total: $14.65");
  });
});

// Note: This test file is for demonstration purposes only.
// In a real project, you would need to set up a proper JavaScript testing environment
// with a framework like Jest to run these tests.