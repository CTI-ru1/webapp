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
    var statements_nodes = Blockly.JavaScript.statementToCode(this, 'nodes');
    // TODO: Assemble JavaScript into code variable.
    var code = '<virtual_node><name>' + virtual_node_name.substring(2, virtual_node_name.length - 2) + '</name><nodes>' + statements_nodes + '</nodes></virtual_node>';
    return code;
};

Blockly.Language.cronschedule= {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(290);
        this.setPreviousStatement(true);
        this.appendDummyInput()
            .appendTitle("Cron Job")
        this.appendDummyInput()
            .appendTitle("Time (s m h)")
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
        this.setNextStatement(true);
        this.setTooltip('');
    }
};


Blockly.Language.oneofschedule= {
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
        this.setNextStatement(true);
        this.setTooltip('');
    }
};


Blockly.JavaScript.cronschedule = function () {
    var text_second = this.getTitleValue('second');
    var text_minute = this.getTitleValue('minute');
    var text_hour = this.getTitleValue('hour');
    var text_dom = this.getTitleValue('dom');
    var text_month = this.getTitleValue('month');
    var text_dow = this.getTitleValue('dow');
    var value_command = Blockly.JavaScript.valueToCode(this, 'command', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var code = "{"
        + "\"second\":\"" + text_second + "\","
        + "\"minute\":\"" + text_minute + "\","
        + "\"hour\":\"" + text_hour + "\","
        + "\"dom\":\"" + text_dom + "\","
        + "\"month\":\"" + text_month + "\","
        + "\"dow\":\"" + text_dow + "\","
        + value_command.replace("(", "").replace(")", "") +
        "}";
    return code;
};



Blockly.JavaScript.oneofschedule = function () {
    var text_second = this.getTitleValue('second');
    var text_minute = this.getTitleValue('minute');
    var text_hour = this.getTitleValue('hour');
    var text_dom = this.getTitleValue('dom');
    var text_month = this.getTitleValue('month');
    var text_dow = this.getTitleValue('dow');
    var value_command = Blockly.JavaScript.valueToCode(this, 'command', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var code = "{"
        + "\"second\":\"" + text_second + "\","
        + "\"minute\":\"" + text_minute + "\","
        + "\"hour\":\"" + text_hour + "\","
        + "\"dom\":\"" + text_dom + "\","
        + "\"month\":\"" + text_month + "\","
        + "\"dow\":\"" + text_dow + "\","
        + "\"payload\":\"" + text_message + "\","
        + value_command.replace("(", "").replace(")", "") +
        "}";
    return code;
};


Blockly.Language.command = {
    helpUrl: 'http://uberdust.cti.gr/rest/help/schedules/',
    init: function () {
        this.setColour(210);
        this.appendDummyInput()
            .appendTitle("Node")
            .appendTitle(new Blockly.FieldDropdown(window.nodes_arr), "node");
        this.appendDummyInput()
            .appendTitle("Capability")
            .appendTitle(new Blockly.FieldDropdown(window.capabilities_arr), "capability");
        this.appendDummyInput()
            .appendTitle("Message")
            .appendTitle(new Blockly.FieldTextInput("0"), "message");
        this.setOutput(true, "String");
        this.setTooltip('');
    }
};
Blockly.JavaScript.command = function () {
    var dropdown_node = this.getTitleValue('node');
    var dropdown_capability = this.getTitleValue('capability');
    var text_message = this.getTitleValue('message');
    // TODO: Assemble JavaScript into code variable.
    var code = "\"node\":\"" + dropdown_node + "\",\"capability\":\"" + dropdown_capability + "\",\"payload\":\"" + text_message + "\"";
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_NONE];
};






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
            .appendTitle("Schedules");
        this.setTooltip('');
    }
};



