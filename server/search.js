const http = require("http")

const { searchAmazon, AmazonSearchResult } = require('unofficial-amazon-search');




http.createServer(function(req, res) {
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
}).listen(1337);