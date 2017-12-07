$(document).ready(function() {

    const dom = {

        init: function() {
            eventApplier.addEventToOrderButton();
            eventApplier.addEventsToAddToCartButtons();
            eventApplier.addEventsToSupplierButtons();
            eventApplier.addEventsToCategoryButtons();
            eventApplier.addEventToSupplierToggle();
            eventApplier.addEventToCategoryToggle();
        }

    };

    const event = {

        proceedToPayment: function(event) {
            event.preventDefault();
            let userName = $('#userName').val();
            let data = {"userName": userName};
            ajax.insertUserData(data, responseHandler.initializePaymentPage);
        },

        addToOrder: function (event) {
            let productId = $(event.target).data("prod_id");
            ajax.insertProductToOrder(productId, responseHandler.updateOrder);
        },

        toggleOrder: function () {
            $("#cart").slideToggle();
        },

        sortBySupplier: function(event) {
            let id = $(event.target).attr("id");
            id = id.replace('supplier', '');
            ajax.getSupplierProducts(id, responseHandler.updateProducts);
        },

        sortByCategory: function(event) {
            let id = $(event.target).attr("id");
            id = id.replace('category', '');
            ajax.getCategoryProducts(id, responseHandler.updateProducts);
        },

        toggleCategories: function() {
            $("#categoryButtons").slideToggle();
        },

        toggleSuppliers: function() {
            $("#supplierButtons").slideToggle();
        },

        addCheckoutForm: function() {
            let form = $('<form/>', {});
            let nameInput = $('<input/>', {
                id: "userName",
                name: "name",
                placeholder: "Name",
            });
            let countryName = $('<input/>', {
                id: "countryName",
                name: "country",
                placeholder: "Country",
            });
            let zipCode = $('<input/>', {
                id: "zipCode",
                name: "zipcode",
                placeholder: "Zip-code",
                pattern: "[0-9]{4,}"
            });
            let address = $('<input/>', {
                id: "address",
                name: "address",
                placeholder: "Address",
            });
            let phoneNumber = $('<input/>', {
                id: "phoneNumber",
                name: "phoneNumber",
                placeholder: "Telephone Number",
                pattern: "[0+]+[0-9]{9,}",
            });
            let emailAddress = $('<input/>', {
                id: "emailAddress",
                name: "emailAddress",
                placeholder: "E-mail",
                pattern: "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$",
            });
            let paymentButton = $('<button/>', {id: "checkout", "class": "btn btn-primary", type: "submit"})
                .text("Pay")
                .click(event.proceedToPayment);
            $('#cart').empty();
            form
                .append(nameInput).append("<br>")
                .append(countryName).append("<br>")
                .append(zipCode).append("<br>")
                .append(address).append("<br>")
                .append(phoneNumber).append("<br>")
                .append(emailAddress).append("<br>")
                .append(paymentButton);
            $('#cart').append(form);
        }

    };

    const eventApplier = {

        addEventsToSupplierButtons: function() {
            let buttons = $("button[id*='supplier']");
            buttons.click(event.sortBySupplier);
        },

        addEventsToCategoryButtons: function() {
            let buttons = $("button[id*='category']");
            console.log(buttons);
            buttons.click(event.sortByCategory);
        },

        addEventToOrderButton: function () {
            $('#cartButton').click(event.toggleOrder);
        },

        addEventsToAddToCartButtons: function() {
            $('.addtocart').click(event.addToOrder);
        },
        addEventToSupplierToggle: function() {
            $("#toggleSupplier").click(event.toggleSuppliers);
        },
        addEventToCategoryToggle: function() {
            $("#toggleCategory").click(event.toggleCategories);
        }

    };

    const elementBuilder = {

        productInOrder: function(name, quantity, price) {
            let wrapper = $('<div/>', {"class": "row"});
            let nameParagraph = $('<p/>', {"class": "col-8"}).text(name);
            let quantityParagraph = $('<p/>', {"class": "col-1"}).text(quantity);
            let priceParagraph = $('<p/>', {"class": "col-3"}).text(price);
            wrapper
                .append(nameParagraph)
                .append(quantityParagraph)
                .append(priceParagraph);

            return wrapper;
        },

        checkoutButton: function() {
            return $('<button/>', {id: "checkout", "class": "btn btn-primary"})
                .text("Checkout")
                .click(event.addCheckoutForm);
        },

        productInList: function(name, description, price, id) {
            let outerWrapper = $('<div/>', {"class": "offset-2 col-xs-4 col-lg-3"});
            let wrapper = $('<div/>', {"class": "thumbnail"});
            let image = $('<img/>', {
                "class": "group list-group-image",
                src: "http://placehold.it/400x250/000/fff",
            });
            let innerWrapper = $('<div/>', {"class": "caption"});
            let productName = $('<h4/>', {"class": "group inner list-group-item-heading"}).text(name);
            let productDescription = $('<p/>', {"class": "group inner list-group-item-text"}).text(description);
            let row = $('<div/>', {"class": "row"});
            let priceWrapper = $('<div/>', {"class": "col-xs-12 col-md-6"});
            let productPrice = $('<p/>', {"class": "lead"}).text(price);
            let addToCartWrapper = $('<div/>', {"class": "col-xs-12 col-md-6"});
            let addToCart = $('<a/>', {
                "class": "btn btn-success addtocart",
                "data-prod_id": id,
            }).text("Add to cart").click(event.addToOrder);
            priceWrapper.append(productPrice);
            addToCartWrapper.append(addToCart);
            row.append(priceWrapper).append(addToCartWrapper);
            innerWrapper.append(productName);
            innerWrapper.append(productDescription);
            innerWrapper.append(row);
            wrapper.append(image);
            wrapper.append(innerWrapper);
            outerWrapper.append(wrapper);

            return outerWrapper;
        }
    };

    const responseHandler = {

        updateOrder: function (response) {
            let itemsNumber = response.itemsNumber;
            let totalPrice = response.totalPrice;
            $('#total-items').text(itemsNumber);
            $('#total-price').text(totalPrice);
            let products = response.shoppingCart;
            let cart = $("#cart");
            cart.empty();
            for (let i = 0; i < products.length;i++) {
                cart.append(elementBuilder.productInOrder(products[i].name, products[i].quantity, products[i].price));
            }
            cart.append(elementBuilder.checkoutButton());
        },

        updateProducts: function(response) {
            $("#collectionName").text(response.collectionName);
            let productDiv = $("#products");
            productDiv.empty();
            let products = response.collection;
            for (let i = 0; products.length; i++) {
                productDiv.append(elementBuilder.productInList(
                    products[i].name, products[i].description, products[i].price, products[i].id));
            }
            eventApplier.addEventsToAddToCartButtons();
        },

        initializePaymentPage: function(response) {
            $('#cart').empty();

            let paymentOptionText = $('<p/>', {"class": "offset-1"}).text("Please choose your payment method you prick!");
            let creditCardPaymentButton = $('<button/>', {id: "creditCardPayment", "class": "btn btn-primary col-4 offset-1"})
                .text("Credit Card");
            creditCardPaymentButton.click( function() {
                let form = $('<form/>', {});
                let nameInput = $('<input/>', {
                    id: "cardHoledName",
                    name: "cardHoler",
                    placeholder: "Enter Card Holer Name",
                });
                let creditCardconfirmationButton = $('<button/>', {id: "creditCardPayment", "class": "btn btn-primary"})
                    .text("Confirm Credit Card Credentials");
                creditCardconfirmationButton.click( function () {
                        let cardHolderName = $('#cardHolderName').val();
                        let data = {"cardHolderName": cardHolderName};
                        ajax.insertCreditCardData(data, function () {
                            $('#cart').empty();
                            let paymentConfirmationText = $('<p/>', {"class": "offset-1"}).text("Thank you for your purchase");
                            $('#cart').append(paymentConfirmationText)
                        });
                });
                $('#cart').empty();
                $('#cart').append(form).append(nameInput).append(creditCardconfirmationButton);
            });
            let payPalPaymentButton = $('<button/>', {id: "payPalPayment", "class": "btn btn-primary col-4 offset-1"})
                .text("Pay Pal");
            payPalPaymentButton.click( function() {
                let form = $('<form/>', {});
                let nameInput = $('<input/>', {
                    id: "payPalUserName",
                    name: "payPalName",
                    placeholder: "Enter Pay Pal User Name",
                });
                let payPalconfirmationButton = $('<button/>', {id: "payPalPayment", "class": "btn btn-primary"})
                    .text("Confirm PayPal Credentials")
                payPalconfirmationButton.click( function () {
                    let cardHolderName = $('#cardHolderName').val();
                    let data = {"cardHolderName": cardHolderName};
                    ajax.insertPayPalData(data, function () {
                        $('#cart').empty();
                        let paymentConfirmationText = $('<p/>', {"class": "offset-1"}).text("Thank you for your purchase");
                        $('#cart').append(paymentConfirmationText)
                    });
                });
                $('#cart').empty();
                $('#cart').append(form).append(nameInput).append(payPalconfirmationButton);
            });
            $('#cart').append(paymentOptionText).append(creditCardPaymentButton).append(payPalPaymentButton);
        }
    };

    const ajax = {

        getSupplierProducts: function(id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/get-supplier-products/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },

        getCategoryProducts: function(id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/get-category-products/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },

        insertProductToOrder: function (id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/add-product/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },

        insertUserData: function(data, responseHandler) {
            $.ajax({
                type: "POST",
                url: "/api/add-user-data",
                data: JSON.stringify(data),
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },

        insertCreditCardData: function(data, responseHandler) {
            $.ajax({
                type: "POST",
                url: "/api/add-credit-card-data",
                data: JSON.stringify(data),
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },

        insertPayPalData: function(data, responseHandler) {
            $.ajax({
                type: "POST",
                url: "/api/add-pay-pal-data",
                data: JSON.stringify(data),
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        }
    };

    dom.init();

});
