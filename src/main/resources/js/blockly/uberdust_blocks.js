Blockly.Language.virtual_node = {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(330);
        this.appendValueInput("name")
            .setCheck("String")
            .setAlign(Blockly.ALIGN_CENTRE)
            .appendTitle("VirtualNodeName");
        this.appendStatementInput("nodes")
            .setCheck("String");
        this.setInputsInline(true);
        this.setTooltip('');
    }
};

Blockly.Language.node = {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(210);
        this.appendValueInput("nodename")
            .setCheck("String")
            .setAlign(Blockly.ALIGN_RIGHT)
            .appendTitle(new Blockly.FieldDropdown(window.nodes_arr), "nodenameslist");
        this.setInputsInline(true);
        this.setPreviousStatement(true, "String");
        this.setNextStatement(true, "String");
        this.setTooltip('');
    }
};
Blockly.Language.node = {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(210);
        this.appendValueInput("nodename")
            .setCheck("String")
            .setAlign(Blockly.ALIGN_RIGHT)
            .appendTitle(new Blockly.FieldDropdown(window.nodes_arr), "nodenameslist");
        this.setInputsInline(true);
        this.setPreviousStatement(true, "String");
        this.setNextStatement(true, "String");
        this.setTooltip('');
    }
};
Blockly.JavaScript.node = function () {
    //var value_nodename = Blockly.JavaScript.valueToCode(this, 'nodename', Blockly.JavaScript.ORDER_ATOMIC);
    var value_nodename = this.getTitleValue('nodenameslist');
    // TODO: Assemble JavaScript into code variable.
    var code = "<node>" + value_nodename + "</node>";
    return code;
};
Blockly.JavaScript.virtual_node = function () {
    var virtual_node_name = Blockly.JavaScript.valueToCode(this, 'name', Blockly.JavaScript.ORDER_ATOMIC);
    //var statements_nodes = Blockly.JavaScript.statementToCode(this, 'nodes');
    var statements_nodes = Blockly.JavaScript.statementToCode(this,'nodes');//, Blockly.JavaScript.ORDER_ATOMIC).replace("(", "").replace(")", "");

    // TODO: Assemble JavaScript into code variable.
    var code = "{\"name\":\"" + virtual_node_name.substring(2, virtual_node_name.length - 2) + "\", \"conditions\":\"[" + statements_nodes.substring(0,statements_nodes.length-1) + "]\"}";
    return code;
};

//SCHEDULE BLOCKS
Blockly.Language.schedule = {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function() {
        this.appendDummyInput()
            .appendTitle("Node")
            .appendTitle(new Blockly.FieldDropdown(window.nodes_arr), "node");
        this.appendDummyInput()
            .appendTitle("Capability")
            .appendTitle(new Blockly.FieldDropdown(window.capabilities_arr), "capability");
        this.appendStatementInput("Event")
            .setCheck("String")
            .setAlign(Blockly.ALIGN_RIGHT)
            .appendTitle("Schedule");
        this.setTooltip('');
    }
};

Blockly.Language.cronschedule= {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(290);
        this.setPreviousStatement(true);
        this.appendDummyInput()
            .appendTitle("Cron Job")
        this.appendDummyInput()
            .appendTitle("Second")
            .appendTitle(new Blockly.FieldTextInput("0"), "second")
            .appendTitle("Minute")
            .appendTitle(new Blockly.FieldTextInput("*"), "minute")
            .appendTitle("Hour")
            .appendTitle(new Blockly.FieldTextInput("*"), "hour");
        this.appendDummyInput()
            .appendTitle("Day of Month")
            .appendTitle(new Blockly.FieldTextInput("*"), "dom")
            .appendTitle("Month")
            .appendTitle(new Blockly.FieldTextInput("*"), "month")
            .appendTitle("Day of Week")
            .appendTitle(new Blockly.FieldTextInput("?"), "dow");
        this.appendDummyInput()
            .appendTitle("Payload")
            .appendTitle(new Blockly.FieldTextInput("0"), "payload");
        this.setTooltip('');
    }
};


Blockly.Language.oneoffschedule= {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(290);
        this.setPreviousStatement(true);
        this.appendDummyInput()
            .appendTitle("One-of Event @")
        this.appendDummyInput()
            .appendTitle("Time")
            .appendTitle(new Blockly.FieldTextInput("0"), "hour")
            .appendTitle(":")
            .appendTitle(new Blockly.FieldTextInput("0"), "minute")
            .appendTitle(":")
            .appendTitle(new Blockly.FieldTextInput("0"), "second");
        this.appendDummyInput()
            .appendTitle("Date")
            .appendTitle(new Blockly.FieldTextInput("31"), "dom")
            .appendTitle("/")
            .appendTitle(new Blockly.FieldTextInput("12"), "month")
            .appendTitle("/")
            .appendTitle(new Blockly.FieldTextInput("2013"), "year");
        this.appendDummyInput()
            .appendTitle("Payload")
            .appendTitle(new Blockly.FieldTextInput("0"), "payload");
        this.setTooltip('');
    }
};


//JAVASCRIPT GENERATOR
Blockly.JavaScript.schedule = function () {
    var dropdown_node = this.getTitleValue('node');
    var dropdown_capability = this.getTitleValue('capability');

    var text_message = Blockly.JavaScript.valueToCode(this, 'Event', Blockly.JavaScript.ORDER_ATOMIC).replace("(", "").replace(")", "");

    // TODO: Assemble JavaScript into code variable.
    //var code = "\"node\":\"" + dropdown_node + "\",\"capability\":\"" + dropdown_capability + "\",\"payload\":\"" + text_message + "\"";
    var code = "{\"node\":\"" + dropdown_node + "\",\"capability\":\"" + dropdown_capability + "\", "+ text_message +"}";
    // TODO: Change ORDER_NONE to the correct strength.
    return code;
};

Blockly.JavaScript.cronschedule = function () {
    var text_second = this.getTitleValue('second');
    var text_minute = this.getTitleValue('minute');
    var text_hour = this.getTitleValue('hour');
    var text_dom = this.getTitleValue('dom');
    var text_month = this.getTitleValue('month');
    var text_dow = this.getTitleValue('dow');
    var text_payload = this.getTitleValue('payload');
    var code = ""
        + "\"type\":\"cron\","
        + "\"second\":\"" + text_second + "\","
        + "\"minute\":\"" + text_minute + "\","
        + "\"hour\":\"" + text_hour + "\","
        + "\"dom\":\"" + text_dom + "\","
        + "\"month\":\"" + text_month + "\","
        + "\"dow\":\"" + text_dow + "\","
        + "\"payload\":\"" + text_payload+ "\"";
    return [code, Blockly.JavaScript.ORDER_NONE];
};



Blockly.JavaScript.oneoffschedule = function () {
    var text_second = this.getTitleValue('second');
    var text_minute = this.getTitleValue('minute');
    var text_hour = this.getTitleValue('hour');
    var text_dom = this.getTitleValue('dom');
    var text_month = this.getTitleValue('month');
    var text_dow = this.getTitleValue('year');
    var text_payload = this.getTitleValue('payload');
    var code = ""
        + "\"type\":\"one\","
        + "\"second\":\"" + text_second + "\","
        + "\"minute\":\"" + text_minute + "\","
        + "\"hour\":\"" + text_hour + "\","
        + "\"dom\":\"" + text_dom + "\","
        + "\"month\":\"" + text_month + "\","
        + "\"dow\":\"" + text_dow + "\","
        + "\"payload\":\"" + text_payload + "\"";
    return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.Language.condition = {
    helpUrl: 'http://www.example.com/',
    init: function() {
        this.setColour(230);
        this.appendDummyInput()
            .appendTitle("condition:")
            .appendTitle(new Blockly.FieldDropdown(window.capabilities_arr), "capability")
            .appendTitle(new Blockly.FieldTextInput("value"), "value");
        this.setPreviousStatement(true, "String");
        this.setNextStatement(true, "String");
        this.setTooltip('');
    }
};

Blockly.JavaScript.condition = function() {
    var dropdown_capability = this.getTitleValue('capability');
    var text_value = this.getTitleValue('value');
    // TODO: Assemble JavaScript into code variable.
    var code = "{'capability':'"+dropdown_capability +"','value':'"+text_value+"'},";
    return code;//[code, Blockly.JavaScript.ORDER_NONE];
};