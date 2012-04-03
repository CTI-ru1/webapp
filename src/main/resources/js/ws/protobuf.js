var Protobuf = $H();

Protobuf.Decoder = Class.create({
  initialize: function(description) {
    this.description = description;
    this.stream = null;
  },

  readUrl: function(url) {
    var req = new XMLHttpRequest();
    req.open('GET', url, false);
    req.overrideMimeType('text/plain; charset=x-user-defined');
    req.send(null);
    if (req.status != 200) throw '[' + req.status + ']' + req.statusText;
    this.stream = req.responseText;
    var bytes = $A();
    for (i = 0; i < this.stream.length; i++) {
      bytes[i] = this.stream.charCodeAt(i) & 0xff;
    }
    return bytes;
  },

  decode: function(url) {
    var ret = {};
    this.stream = this.readUrl(url);
    while (this.stream.length != 0) {
      var [type, tag] = this.readKey();
      var field = this.description[tag];
      if (!field) throw 'Invalid tag: ' + tag;
      switch (type) {
      case Protobuf.WireType.VARINT:
        ret[field.name] = this.readVarint();
        break;
      case Protobuf.WireType.BIT64:
        throw 'Not yet';
        break;
      case Protobuf.WireType.LENGTH_DELIMITED:
        var data = this.readLengthDelimited();
        if (field.type == 'string') {
          ret[field.name] = String.fromCharCode.apply(String, data);
        }
        else {
          throw 'Not yet';
        }
        break;
      case Protobuf.WireType.START_GROUP:
        throw 'Not yet';
        break;
      case Protobuf.WireType.END_GROUP:
        throw 'Not yet';
        break;
      case Protobuf.WireType.BIT32:
        throw 'Not yet';
        break;
      default:
        throw 'Unknown WireType: ' + type;
      }
    }
    return ret;
  },

  readKey: function() {
    var byte = this.stream.shift();
    var type = byte & 0x07;
    var tag = (byte & 0x7F) >>> 3;
    var first = true;
    if ((byte >> 7) != 0) {
      byte = this.stream.shift();
      tag = ((byte & 0x7F) << (first ? 4 : 7)) | tag;
    }
    return [type, tag];
  },

  readVarint: function() {
    var ret = 0;
    for (var i = 0; ; i++) {
      var byte = this.stream.shift();
      ret |= (byte & 0x7F) << (7 * i);
      if ((byte >> 7) == 0) break;
    }
    return ret;
  },

  readLengthDelimited: function() {
    var length = this.readVarint();
    var ret = $A();
    for (var i = 0; i < length; i++) {
      ret.push(this.stream.shift());
    }
    return ret;
  },

  // Methods for debug
  toBinaryString: function(val, figure) {
    var ret = '';
    var block = function() {
      ret = '' + (val & 1) + ret;
      val >>>= 1;
    };
    if (figure == null) {
      while (val != 0)  block();
    } else {
      for (var i = 0; i < 8; i++) block();
    }
    return ret;
  },
});

Protobuf.WireType = {
  VARINT           : 0,
  BIT64            : 1,
  LENGTH_DELIMITED : 2,
  START_GROUP      : 3,
  END_GROUP        : 4,
  BIT32            : 5,
};

