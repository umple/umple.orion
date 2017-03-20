var provider = new orion.PluginProvider({ 
    name: "Umple Plugin", 
    version: "1.0", 
    description: "Tools to support Umple developemnt" 
});

/* Umple code generation service */
// adds the ability to generate files from Umple code
var umpleGenerate = { 
  execute: function(editorContext, options){

      // TODO: the location of the Orion/UmpleCodeGenerator server
      var umpleGenerateServerURL = "http://localhost:4567/UmpleGenerate";

      // request is the currently selected file
      var request = options.input;

      // build and send the request
      var x = new XMLHttpRequest();
      x.open("POST", umpleGenerateServerURL, true);               
      x.send(request);
  }
};

var umpleGenerateProperties= { 
  name: "Umple Plugin",
  key: ["u", true, true] // Ctrl+Shift+u
};

provider.registerService("orion.edit.command", umpleGenerate, umpleGenerateProperties);

/* Umple content type service */
// Adds a new content type that recognizes Umple textual models
provider.registerService("orion.core.contenttype", {}, {
  contentTypes: [
    {
      id: "text/umple",
      name: "Textual Umple Model",
      extension: ["ump"],
      "extends": "text/plain" // extends must be a string as it is otherwise reserved in Javascript
    }
  ]
});

// Adds a grammar for highlighting code and keywords in Umple models
provider.registerService("orion.edit.highlighter", {}, {
  id: "orion.umple",
  contentTypes: ["text/umple"],
  patterns: [
    // Matcing braces and parenthesis from Orion's library
    { include: "orion.lib#string_doubleQuote" },
    { include: "orion.lib#string_singleQuote" },
    { include: "orion.lib#brace_open" },
    { include: "orion.lib#brace_close" },
    { include: "orion.lib#bracket_open" },
    { include: "orion.lib#bracket_close" },
    { include: "orion.lib#parenthesis_open" },
    { include: "orion.lib#parenthesis_close" },

    // Numbers and c-style comments, which Umple uses.
    { include: "orion.lib#number_decimal" },
    { include: "orion.c-like#comments" },

    // Keywords
    {
      name: "keyword.other.reserved",
      match: "\\b(?:class|interface|namespace|external|generate|strictness|association|trait|depend|use|isA|lazy|immutable|const|settable|internal|autounique|defaulted|before|after|Integer|Float|String|Double|Boolean|Date|Time|->|--|entry|do|exit|singleton|trace|tracer)\\b"
    },

    // For extra code, straight import for now like in UmpleOnline.
    { include: "orion.java" },
    { include: "orion.cpp" },
    { include: "orion.php" }
  ]
});

// Connect to Orion so that services are active
provider.connect();
