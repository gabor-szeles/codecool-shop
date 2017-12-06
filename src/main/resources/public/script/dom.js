$(document).ready(function() {

    const dom = {

        init: function() {
            eventApplier.addEventToOrderButton();
            eventApplier.addEventsToAddToCartButtons();
            eventApplier.addEventsToSupplierButtons();
        }

    };

    const event = {

        addToOrder: function (event) {
            let productId = $(event.target).data("prod_id");
            console.log(productId);
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

    };

    const eventApplier = {

        addEventsToSupplierButtons: function() {
            let buttons = $("button[id*='supplier']");
            buttons.click(event.sortBySupplier);
        },

        addEventToOrderButton: function () {
            $('#cartButton').click(event.toggleOrder);
        },

        addEventsToAddToCartButtons: function() {
            $('.addtocart').click(event.addToOrder);
        },

    };

    const elementBuilder = {
        
        productInOrder: function(name, quantity, price) {
            let wrapper = $('<div/>', {"class": "row"});
            let nameParagraph = $('<p/>', {}).text(name);
            let quantityParagraph = $('<p/>', {}).text(quantity);
            let priceParagraph = $('<p/>', {}).text(price);
            wrapper
                .append(nameParagraph)
                .append(quantityParagraph)
                .append(priceParagraph);

            return wrapper;
        },

        productInList: function(name, description, price, id) {
            let outerWrapper = $('<div/>', {"class": "item col-xs-4 col-lg-4"});
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

        insertProductToOrder: function (id, responseHandler) {
            $.ajax({
                type: "GET",
                url: "/api/add-product/" + id,
                dataType: "json",
                contentType: "application/json",
                success: responseHandler
            });
        },
    };

    dom.init();

});
