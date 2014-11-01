# scarlett

Provides macros to declare vars in namespaces other than *ns*

To be used sparingly to overcome otherwise cyclic namespace dependencies

## Usage

(ns-declare my-module startup shutdown)

(defn start-cli
  [& args]
  (my-module/startup args))

(declare+ module-a/startup module-b/startup)

## License

Copyright © 2014 Stephen C. Gilardi

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
