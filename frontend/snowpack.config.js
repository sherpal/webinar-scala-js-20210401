// Snowpack Configuration File
// See all supported options: https://www.snowpack.dev/reference/configuration

/** @type {import("snowpack").SnowpackUserConfig } */
const httpProxy = require("http-proxy");
const proxy = httpProxy.createServer({ target: "http://localhost:9000" });

module.exports = {
  packageOptions: {
    polyfillNode: true,
  },
  buildOptions: {
    out: "../server/src/main/resources",
  },
  mount: {
    public: "/",
    "target/scala-2.13/frontend-fastopt": "/",
    "src/main/resources": "/",
  },

  routes: [
    {
      src: "/api/.*",
      dest: (req, res) => {
        // remove /api prefix (optional)
        //req.url = req.url.replace(/^\/api/, '');
        proxy.web(req, res);
      },
    },
  ],
};
