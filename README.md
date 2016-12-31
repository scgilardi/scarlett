# scarlett

Provides macros to declare vars in namespaces other than `*ns*`

To be used sparingly to overcome otherwise cyclic namespace dependencies

## Usage

```clojure
(ns-declare my-module startup shutdown)
```

declares `my-module/startup` and `my-module/shutdown`


```clojure
(declare+ module-a/startup module-b/startup)
```

declares `module-a/startup` and `module-b/startup`

## License

Copyright © 2016 Stephen C. Gilardi

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
