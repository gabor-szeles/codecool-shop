// $(function () {
$(document).ready(function() {

    const dom = {
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
                            "class": "btn btn-success addtocart"
                        }).text("Add To Cart");
                        priceWrapper.append(price);
                        addToCartWrapper.append(addToCart);
                        row.append(priceWrapper).append(addToCartWrapper);

                        outerWrapper
                            .append(wrapper)
                            .append(image)
                            .append(innerWrapper)
                            .append(productName)
                            .append(productDescription)
                            .append(row);
                        productDiv.append(outerWrapper);
                    }
                }
            });
        },
        addEventsToSupplierButtons: function() {
            let buttons = $("button[id*='supplier']");
            buttons.click(dom.supplierButtonClickHandler);
        }
    };

    dom.addEventsToSupplierButtons();
});

// });
