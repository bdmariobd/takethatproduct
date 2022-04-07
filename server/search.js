const express = require("express")

//library unofficial amazon search
//https://github.com/kyle-n/unofficial-amazon-search
//https://www.npmjs.com/package/unofficial-amazon-search
const { searchAmazon, AmazonSearchResult } = require('unofficial-amazon-search');



var app = express();
const PORT = process.env.PORT || 5000

app.use(express.static('public'));
app.set('json spaces', 2)

app.get('/', function(req, res) {
    res.send('Welcome Home');
});


app.get("/search", function(req, res) {

    try {
        let search = req.query.q;

        searchAmazon(search).then(data => {

            let json = [];

            //console.log(data.searchResults)
            for (let i = 0; i < data.searchResults.length; i++) {
                let item = data.searchResults[i];
                let prices = [];
                item.prices.forEach(price => {
                    prices.push(price);
                });
                json.push({
                    title: item.title,
                    imageUrl: item.imageUrl,
                    productUrl: item.fullProductUrl,
                    prices: prices
                });
            }
            //console.log(JSON.stringify(json))
            res.status(200).type('json').send(JSON.stringify(json.filter(item => item.prices.length > 0)));
        });
    } catch (err) {
        console.log(err);
        res.status(500).send(err);
    }
});

// Handle 404 - Keep this as a last route
app.use(function(req, res, next) {
    res.status(404);
    res.send('404: File Not Found');
});

app.use(function(errr, req, res) {
    res.status(500);
    res.send('500:' + errr);
});

app.listen(PORT, function() {
    console.log(`Example app listening on port ${PORT}!`);
});



/* http.createServer(function(req, res) {
    res.writeHead(200, { 'Content-Type': "application/json" });

    searchAmazon('television').then(data => {

        let json = [];

        console.log(data.searchResults)
        for (let i = 0; i < data.searchResults.length; i++) {
            let item = data.searchResults[i];
            json.push({
                title: item.title,
                imageUrl: item.imageUrl,
                productUrl: item.productUrl
            });
        }
        console.log(JSON.stringify(json))
        res.end(JSON.stringify(json));
    });
}).listen(1337); */