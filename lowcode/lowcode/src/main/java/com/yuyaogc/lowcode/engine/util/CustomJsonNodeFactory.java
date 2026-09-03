package com.yuyaogc.lowcode.engine.util;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomJsonNodeFactory extends JsonNodeFactory {

    private static final long serialVersionUID = 8807395553661461181L;

    private final JsonNodeFactory delegate;
    private final CustomParserFactory parserFactory;

    /**
     * "Why isn't this a map?" you might be wondering. Well, when the nodes are created, they're all
     * empty and a node's hashCode is based on its children. So if you use a map and put the node
     * in, then the node's hashCode is based on no children, then when you lookup your node, it is
     * *with* children, so the hashcodes are different. Instead of all of this, you have to iterate
     * through a listing and find their matches once the objects have been populated, which is only
     * after the document has been completely parsed
     */
    private List<Map.Entry<JsonNode, JsonLocation>> locationMapping;

    public CustomJsonNodeFactory(JsonNodeFactory nodeFactory,
                                 CustomParserFactory parserFactory) {
        delegate = nodeFactory;
        this.parserFactory = parserFactory;
        locationMapping = new ArrayList<>();
    }

    /**
     * Given a node, find its location, or null if it wasn't found
     *
     * @param jsonNode the node to search for
     * @return the location of the node or null if not found
     */
    public JsonLocation getLocationForNode(JsonNode jsonNode) {
        return this.locationMapping.stream().filter(e -> e.getKey().equals(jsonNode))
                .map(e -> e.getValue()).findAny().orElse(null);
    }

    /**
     * Simple interceptor to mark the node in the lookup list and return it back
     *
     * @param <T>  the type of the JsonNode
     * @param node the node itself
     * @return the node itself, having marked its location
     */
    private <T extends JsonNode> T markNode(T node) {
        JsonLocation loc = parserFactory.getParser().getCurrentLocation();
        locationMapping.add(new AbstractMap.SimpleEntry<>(node, loc));
        return node;
    }

    @Override
    public BooleanNode booleanNode(boolean v) {
        return markNode(delegate.booleanNode(v));
    }

    @Override
    public NullNode nullNode() {
        return markNode(delegate.nullNode());
    }

    @Override
    public NumericNode numberNode(byte v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Byte value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public NumericNode numberNode(short v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Short value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public NumericNode numberNode(int v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Integer value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public NumericNode numberNode(long v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Long value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public ValueNode numberNode(BigInteger v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public NumericNode numberNode(float v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Float value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public NumericNode numberNode(double v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public ValueNode numberNode(Double value) {
        return markNode(delegate.numberNode(value));
    }

    @Override
    public ValueNode numberNode(BigDecimal v) {
        return markNode(delegate.numberNode(v));
    }

    @Override
    public TextNode textNode(String text) {
        return markNode(delegate.textNode(text));
    }

    @Override
    public BinaryNode binaryNode(byte[] data) {
        return markNode(delegate.binaryNode(data));
    }

    @Override
    public BinaryNode binaryNode(byte[] data, int offset, int length) {
        return markNode(delegate.binaryNode(data, offset, length));
    }

    @Override
    public ValueNode pojoNode(Object pojo) {
        return markNode(delegate.pojoNode(pojo));
    }

    @Override
    public ValueNode rawValueNode(RawValue value) {
        return markNode(delegate.rawValueNode(value));
    }

    @Override
    public ArrayNode arrayNode() {
        return markNode(delegate.arrayNode());
    }

    @Override
    public ArrayNode arrayNode(int capacity) {
        return markNode(delegate.arrayNode(capacity));
    }

    @Override
    public ObjectNode objectNode() {
        return markNode(delegate.objectNode());
    }

}