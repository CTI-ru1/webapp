### Generated by rprotoc. DO NOT EDIT!
### <proto file: /home/amaxilatis/repositories/uberdust/libraries/WebSocketsLibrary/src/main/protobuf/message.proto>
# package communication;
# 
# option java_package = "eu.uberdust.communication.protobuf";
# option java_outer_classname = "Message";
# 
# message Control {
#     required string destination = 1;
#     optional string capability = 2;
#     optional string lastValue = 3;
#     optional string payload = 4;
# }
# 
# message NodeReadings {
# 
#     message Reading {
# 
#         required string node = 1;
#         required string capability = 2;
#         required int64 timestamp = 3;
#         optional string stringReading = 4;
#         optional double doubleReading = 5;
#     }
# 
#   repeated Reading reading = 1;
# }
# 
# message LinkReadings{
# 
#     message Reading {
# 
#         required string source = 1;
#         required string target = 2;
#         required string capability = 3;
#         required int64 timestamp = 4;
#         optional string stringReading = 5;
#         optional double doubleReading = 6;
#     }
# 
#     repeated Reading reading = 1;
# }
# 
# message Envelope{
# 
#    enum Type{
#        CONTROL = 0;
#        NODE_READINGS = 1;
#        LINK_READINGS = 2;
#      }
# 
#   required Type type = 1 [default = CONTROL];
#   optional Control control = 2;
#   optional NodeReadings nodeReadings = 3;
#   optional LinkReadings linkReadings = 4;
# 
# }
require 'protobuf/message/message'
require 'protobuf/message/enum'
require 'protobuf/message/service'
require 'protobuf/message/extend'

module Communication
  ::Protobuf::OPTIONS[:"java_package"] = "eu.uberdust.communication.protobuf"
  ::Protobuf::OPTIONS[:"java_outer_classname"] = "Message"
  class Control < ::Protobuf::Message
    defined_in __FILE__
    required :string, :destination, 1
    optional :string, :capability, 2
    optional :string, :lastValue, 3
    optional :string, :payload, 4
  end
  class NodeReadings < ::Protobuf::Message
    defined_in __FILE__
    class Reading < ::Protobuf::Message
      defined_in __FILE__
      required :string, :node, 1
      required :string, :capability, 2
      required :int64, :timestamp, 3
      optional :string, :stringReading, 4
      optional :double, :doubleReading, 5
    end
    repeated :Reading, :reading, 1
  end
  class LinkReadings < ::Protobuf::Message
    defined_in __FILE__
    class Reading < ::Protobuf::Message
      defined_in __FILE__
      required :string, :source, 1
      required :string, :target, 2
      required :string, :capability, 3
      required :int64, :timestamp, 4
      optional :string, :stringReading, 5
      optional :double, :doubleReading, 6
    end
    repeated :Reading, :reading, 1
  end
  class Envelope < ::Protobuf::Message
    defined_in __FILE__
    class Type < ::Protobuf::Enum
      defined_in __FILE__
      CONTROL = value(:CONTROL, 0)
      NODE_READINGS = value(:NODE_READINGS, 1)
      LINK_READINGS = value(:LINK_READINGS, 2)
    end
    required :Type, :type, 1, :default => :CONTROL
    optional :Control, :control, 2
    optional :NodeReadings, :nodeReadings, 3
    optional :LinkReadings, :linkReadings, 4
  end
end