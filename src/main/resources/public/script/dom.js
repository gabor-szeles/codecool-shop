$(document).ready(function() {

    const dom = {
        addToCartClick: function (event) {
            var clickedButton = event.target;
            var productId = {"productid" : clickedButton.dataset.prod_id};
            $.ajax({
                type: "POST",
                url: "/api/additem",  // Send the login info to this page
                data: JSON.stringify(productId),
                dataType: "json",
                contentType: "application/json",
                success: dom.refreshHeader,
            });
        },
        reviewCart: function () {
            $("#cart").slideToggle();
        },
        drawReview: function (response) {
            var list = response.shoppingCart;
            let cart = $("#cart");
            for (let i = 0; i<list.length;i++) {
                let wrapper = $('<div/>', {
                    "class": "row"
                });
                let name = $('<p/>', {}).text(list[i].name);
                let quantity = $('<p/>', {}).text(list[i].quantity);
                let price = $('<p/>', {}).text(list[i].price);
                wrapper
                    .append(name)
                    .append(quantity)
                    .append(price);
                cart.hide();
                cart.append(wrapper).slideToggle();
                cart.click(function(event) {
                    $(event.target).slideToggle();
                })
            }
        },
        supplierButtonClickHandler: function(event) {
            let id = $(event.target).attr("id");

            id = id.replace('supplier', '');
            console.log("id: " + id);
            let data = {"id": id};
            $.ajax({
                type: "GET",
                url: "/api/get-supplier-products/" + id,
                dataType: "json",
                contentType: "application/json",
                success: function(response) {
                    console.log(response);
                    $("#collectionName").text(response.collectionName);
                    let productDiv = $("#products");
                    productDiv.empty();
                    console.log(response.collection);
                    for (let i = 0; response.collection.length; i++) {
                        let outerWrapper = $('<div/>', {
                            "class": "item col-xs-4 col-lg-4",
                        });
                        let wrapper = $('<div/>', {
                            "class": "thumbnail",
                        });
                        let image = $('<img/>', {
                            "class": "group list-group-image",
                            src: "http://placehold.it/400x250/000/fff",
                        });
                        let innerWrapper = $('<div/>', {
                            "class": "caption",
                        });
                        let productName = $('<h4/>', {
                            "class": "group inner list-group-item-heading"
                        }).text(response.collection[i].name);
                        let productDescription = $('<p/>', {
                            "class": "group inner list-group-item-text",
                        }).text(response.collection[i].description);
                        let row = $('<div/>', {
                            "class": "row"
                        });
                        let priceWrapper = $('<div/>', {
                            "class": "col-xs-12 col-md-6"
                        });
                        let price = $('<p/>', {
                            "class": "lead"
                        }).text(response.collection[i].price);
                        let addToCartWrapper = $('<div/>', {
                            "class": "col-xs-12 col-md-6",
                        });
                        let addToCart = $('<a/>', {
                            "class": "btn btn-success addtocart",
                            "data-prod_id": response.collection[i].id,
                        }).text("Add To Cart").click(dom.addToCartClick);
                        priceWrapper.append(price);
                        addToCartWrapper.append(addToCart);
                        row.append(priceWrapper).append(addToCartWrapper);
                        innerWrapper.append(productName);
                        innerWrapper.append(productDescription);
                        innerWrapper.append(row);
                        wrapper.append(image);
                        wrapper.append(innerWrapper);
                        outerWrapper.append(wrapper);
                        productDiv.append(outerWrapper);
                        console.log("lul");
                    }
                    dom.addEventListenersToAddToCart();
                }
            });
        },
        addEventsToSupplierButtons: function() {
            let buttons = $("button[id*='supplier']");
            buttons.click(dom.supplierButtonClickHandler);
        },
        refreshHeader: function (response) {
            console.log(response);
            var itemsNumber = response.itemsNumber;
            var totalPrice = response.totalPrice;
            $('#total-items').text(itemsNumber);
            $('#total-price').text(totalPrice);
            var list = response.shoppingCart;
            console.log("list " + list);
            let cart = $("#cart");
            cart.empty();
            for (let i = 0; i<list.length;i++) {
                let wrapper = $('<div/>', {
                    "class": "row"
                });
                let name = $('<p/>', {}).text(list[i].name);
                let quantity = $('<p/>', {}).text(list[i].quantity);
                let price = $('<p/>', {}).text(list[i].price);
                wrapper
                    .append(name)
                    .append(quantity)
                    .append(price);
                cart.append(wrapper);
                // cart.hide();
                // cart.append(wrapper).slideToggle();
                // cart.click(function(event) {
                //     $(event.target).slideToggle();
                // })
            }
        },
        addEventListenersToHeader: function () {
            $('#cartButton').click(dom.reviewCart);
        },
        addEventListenersToAddToCart: function() {
            $('.addtocart').click(dom.addToCartClick);
        },


    };

    dom.addEventListenersToHeader();
    dom.addEventListenersToAddToCart();
    dom.addEventsToSupplierButtons();
});
