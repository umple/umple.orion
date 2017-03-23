// Location of the Umple server
var umpleGenerationServerURL = "http://localhost:4567/UmpleGenerate";

// Location of this file on the web
var thisFile = location.protocol + '//' + location.host + location.pathname;
var pwd = thisFile.substring(0, thisFile.lastIndexOf("/") + 1);

// Service provider for Orion
var provider = new orion.PluginProvider({ 
    name: "Umple Plugin", 
    version: "1.0", 
    description: "Tools to support Umple developemnt" 
});

//================================
// Generation Services
//================================

// Request umple server to generate files in language
// Helper for all umple generation services
function umpleGenerate(language) { 
  return {
      language: language,
      execute: function(editorContext, options){

          // request format is <language>\n<filename>
          var request = this.language + '\n' + options.input;

          // build and send the request
          var x = new XMLHttpRequest();
          x.open("POST", umpleGenerationServerURL, true);               
          x.send(request);
      }
  }
};

/* Umple generic generate service */
provider.registerService("orion.edit.command", 
    umpleGenerate(""),
    { 
      name: "Umple Generate",
      tooltip: "Generate code from your Textual Umple Model",
      img: pwd + "umple.ico",
      contentType: ["text/umple"]
    }
);

/* Umple Java generate service */
provider.registerService("orion.edit.command", 
    umpleGenerate("Java"),
    { 
      name: "Generate Java",
      tooltip: "Generate Java code from your Textual Umple Model",
      img: pwd + "umple.ico",
      contentType: ["text/umple"]
    }
);

/* Umple Php generate service */
provider.registerService("orion.edit.command", 
    umpleGenerate("Php"),
    { 
      name: "Generate PHP",
      tooltip: "Generate PHP code from your Textual Umple Model",
      img: pwd + "umple.ico",
      contentType: ["text/umple"]
    }
);

/* Umple RTCpp generate service */
provider.registerService("orion.edit.command", 
    umpleGenerate("RTCpp"),
    { 
      name: "Generate RTC++",
      tooltip: "Generate RTC++ code from your Textual Umple Model",
      img: pwd + "umple.ico",
      contentType: ["text/umple"]
    }
);

/* Umple Ruby generate service */
provider.registerService("orion.edit.command", 
    umpleGenerate("Ruby"),
    { 
      name: "Generate Ruby",
      tooltip: "Generate Ruby code from your Textual Umple Model",
      img: pwd + "umple.ico",
      contentType: ["text/umple"]
    }
);

//================================
// Content Type Services
//================================

/* Umple content type service */
// Adds a new content type that recognizes Umple textual models
provider.registerService("orion.core.contenttype", {}, {
  contentTypes: [
    {
      id: "text/umple",
      name: "Textual Umple Model",
      extension: ["ump"],
      "extends": "text/plain", // extends must be a string as it is otherwise reserved in Javascript
      image: pwd + "umple.ico"
    }
  ]
});

/* Umple syntax hilighting service */
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

    // For non-umple code embedded in umple files
    { include: "orion.java" },
    { include: "orion.cpp" },
    { include: "orion.php" }
  ]
});

// Connect to Orion so that services are active
provider.connect();
