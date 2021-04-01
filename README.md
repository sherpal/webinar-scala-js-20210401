# Web application in Scala only

This is the result of a live coding session organised by [B12 Consulting](https://www.b12-consulting.com/).

This repo demonstrates the bare bones of a full stack scala application. In this case, the backend uses the [Play framework](https://www.playframework.com/) as web server, and [Laminar](https://laminar.dev/) as frontend library.

## Run it!

In order to run it, you need to have sbt and node.js installed. I'm unsure what is the minimal version of node.js required, but mine is 14 and it works.

To run the repo in its current state, follow the following steps:

- in one command line/terminal, run `sbt server/run`
- in another one, run `~frontend/fastLinkJS` within sbt.
- in yet another one (the last one), go to `./frontend`, then run `npm install`, then `npx snowpack dev`

You should be automatically redirected to your browser at `http://localhost:8080`.

## Further comments

During the webinar, we did not have the time to mention [ScalablyTyped](https://scalablytyped.org/docs/readme.html), which is the sbt plugin used to generate all [facades types](https://www.scala-js.org/doc/interoperability/facade-types.html) from existing TypeScript typings.


