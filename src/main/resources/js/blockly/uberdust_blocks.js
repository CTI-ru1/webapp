Blockly.Language.virtual_node = {
    helpUrl: 'http://www.example.com/',
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
    helpUrl: 'http://www.example.com/',
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
    helpUrl: 'http://www.example.com/',
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
