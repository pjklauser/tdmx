// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common.proto

package org.tdmx.server.pcs.protobuf;

public final class Common {
  private Common() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  /**
   * Protobuf enum {@code ObjectType}
   */
  public enum ObjectType
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>Message = 0;</code>
     */
    Message(0, 0),
    /**
     * <code>DeliveryReply = 1;</code>
     */
    DeliveryReply(1, 1),
    /**
     * <code>Authorization = 2;</code>
     */
    Authorization(2, 2),
    /**
     * <code>DestinationSession = 3;</code>
     */
    DestinationSession(3, 3),
    /**
     * <code>FlowControl = 4;</code>
     */
    FlowControl(4, 4),
    ;

    /**
     * <code>Message = 0;</code>
     */
    public static final int Message_VALUE = 0;
    /**
     * <code>DeliveryReply = 1;</code>
     */
    public static final int DeliveryReply_VALUE = 1;
    /**
     * <code>Authorization = 2;</code>
     */
    public static final int Authorization_VALUE = 2;
    /**
     * <code>DestinationSession = 3;</code>
     */
    public static final int DestinationSession_VALUE = 3;
    /**
     * <code>FlowControl = 4;</code>
     */
    public static final int FlowControl_VALUE = 4;


    public final int getNumber() { return value; }

    public static ObjectType valueOf(int value) {
      switch (value) {
        case 0: return Message;
        case 1: return DeliveryReply;
        case 2: return Authorization;
        case 3: return DestinationSession;
        case 4: return FlowControl;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ObjectType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<ObjectType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ObjectType>() {
            public ObjectType findValueByNumber(int number) {
              return ObjectType.valueOf(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return org.tdmx.server.pcs.protobuf.Common.getDescriptor().getEnumTypes().get(0);
    }

    private static final ObjectType[] VALUES = values();

    public static ObjectType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }

    private final int index;
    private final int value;

    private ObjectType(int index, int value) {
      this.index = index;
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:ObjectType)
  }

  public interface AttributeValueOrBuilder extends
      // @@protoc_insertion_point(interface_extends:AttributeValue)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required .AttributeValue.AttributeId name = 1;</code>
     */
    boolean hasName();
    /**
     * <code>required .AttributeValue.AttributeId name = 1;</code>
     */
    org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId getName();

    /**
     * <code>optional int64 value = 2;</code>
     */
    boolean hasValue();
    /**
     * <code>optional int64 value = 2;</code>
     */
    long getValue();
  }
  /**
   * Protobuf type {@code AttributeValue}
   */
  public static final class AttributeValue extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:AttributeValue)
      AttributeValueOrBuilder {
    // Use AttributeValue.newBuilder() to construct.
    private AttributeValue(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private AttributeValue(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final AttributeValue defaultInstance;
    public static AttributeValue getDefaultInstance() {
      return defaultInstance;
    }

    public AttributeValue getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private AttributeValue(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId value = org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                name_ = value;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              value_ = input.readInt64();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.tdmx.server.pcs.protobuf.Common.internal_static_AttributeValue_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.tdmx.server.pcs.protobuf.Common.internal_static_AttributeValue_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.tdmx.server.pcs.protobuf.Common.AttributeValue.class, org.tdmx.server.pcs.protobuf.Common.AttributeValue.Builder.class);
    }

    public static com.google.protobuf.Parser<AttributeValue> PARSER =
        new com.google.protobuf.AbstractParser<AttributeValue>() {
      public AttributeValue parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new AttributeValue(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<AttributeValue> getParserForType() {
      return PARSER;
    }

    /**
     * Protobuf enum {@code AttributeValue.AttributeId}
     */
    public enum AttributeId
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>AccountZoneId = 0;</code>
       */
      AccountZoneId(0, 0),
      /**
       * <code>ZoneId = 1;</code>
       */
      ZoneId(1, 1),
      /**
       * <code>DomainId = 2;</code>
       */
      DomainId(2, 2),
      /**
       * <code>ChannelId = 3;</code>
       */
      ChannelId(3, 3),
      /**
       * <code>TemporaryChannelId = 4;</code>
       */
      TemporaryChannelId(4, 4),
      /**
       * <code>ServiceId = 5;</code>
       */
      ServiceId(5, 5),
      /**
       * <code>AddressId = 6;</code>
       */
      AddressId(6, 6),
      /**
       * <code>AuthorizationId = 7;</code>
       */
      AuthorizationId(7, 7),
      /**
       * <code>FlowQuotaId = 8;</code>
       */
      FlowQuotaId(8, 8),
      /**
       * <code>MessageId = 9;</code>
       */
      MessageId(9, 9),
      /**
       * <code>DeliveryReportId = 10;</code>
       */
      DeliveryReportId(10, 10),
      ;

      /**
       * <code>AccountZoneId = 0;</code>
       */
      public static final int AccountZoneId_VALUE = 0;
      /**
       * <code>ZoneId = 1;</code>
       */
      public static final int ZoneId_VALUE = 1;
      /**
       * <code>DomainId = 2;</code>
       */
      public static final int DomainId_VALUE = 2;
      /**
       * <code>ChannelId = 3;</code>
       */
      public static final int ChannelId_VALUE = 3;
      /**
       * <code>TemporaryChannelId = 4;</code>
       */
      public static final int TemporaryChannelId_VALUE = 4;
      /**
       * <code>ServiceId = 5;</code>
       */
      public static final int ServiceId_VALUE = 5;
      /**
       * <code>AddressId = 6;</code>
       */
      public static final int AddressId_VALUE = 6;
      /**
       * <code>AuthorizationId = 7;</code>
       */
      public static final int AuthorizationId_VALUE = 7;
      /**
       * <code>FlowQuotaId = 8;</code>
       */
      public static final int FlowQuotaId_VALUE = 8;
      /**
       * <code>MessageId = 9;</code>
       */
      public static final int MessageId_VALUE = 9;
      /**
       * <code>DeliveryReportId = 10;</code>
       */
      public static final int DeliveryReportId_VALUE = 10;


      public final int getNumber() { return value; }

      public static AttributeId valueOf(int value) {
        switch (value) {
          case 0: return AccountZoneId;
          case 1: return ZoneId;
          case 2: return DomainId;
          case 3: return ChannelId;
          case 4: return TemporaryChannelId;
          case 5: return ServiceId;
          case 6: return AddressId;
          case 7: return AuthorizationId;
          case 8: return FlowQuotaId;
          case 9: return MessageId;
          case 10: return DeliveryReportId;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<AttributeId>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<AttributeId>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<AttributeId>() {
              public AttributeId findValueByNumber(int number) {
                return AttributeId.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return org.tdmx.server.pcs.protobuf.Common.AttributeValue.getDescriptor().getEnumTypes().get(0);
      }

      private static final AttributeId[] VALUES = values();

      public static AttributeId valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private AttributeId(int index, int value) {
        this.index = index;
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:AttributeValue.AttributeId)
    }

    private int bitField0_;
    public static final int NAME_FIELD_NUMBER = 1;
    private org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId name_;
    /**
     * <code>required .AttributeValue.AttributeId name = 1;</code>
     */
    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required .AttributeValue.AttributeId name = 1;</code>
     */
    public org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId getName() {
      return name_;
    }

    public static final int VALUE_FIELD_NUMBER = 2;
    private long value_;
    /**
     * <code>optional int64 value = 2;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional int64 value = 2;</code>
     */
    public long getValue() {
      return value_;
    }

    private void initFields() {
      name_ = org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId.AccountZoneId;
      value_ = 0L;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasName()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, name_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, value_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, name_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, value_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.tdmx.server.pcs.protobuf.Common.AttributeValue parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.tdmx.server.pcs.protobuf.Common.AttributeValue prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code AttributeValue}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:AttributeValue)
        org.tdmx.server.pcs.protobuf.Common.AttributeValueOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.tdmx.server.pcs.protobuf.Common.internal_static_AttributeValue_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.tdmx.server.pcs.protobuf.Common.internal_static_AttributeValue_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.tdmx.server.pcs.protobuf.Common.AttributeValue.class, org.tdmx.server.pcs.protobuf.Common.AttributeValue.Builder.class);
      }

      // Construct using org.tdmx.server.pcs.protobuf.Common.AttributeValue.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId.AccountZoneId;
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.tdmx.server.pcs.protobuf.Common.internal_static_AttributeValue_descriptor;
      }

      public org.tdmx.server.pcs.protobuf.Common.AttributeValue getDefaultInstanceForType() {
        return org.tdmx.server.pcs.protobuf.Common.AttributeValue.getDefaultInstance();
      }

      public org.tdmx.server.pcs.protobuf.Common.AttributeValue build() {
        org.tdmx.server.pcs.protobuf.Common.AttributeValue result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.tdmx.server.pcs.protobuf.Common.AttributeValue buildPartial() {
        org.tdmx.server.pcs.protobuf.Common.AttributeValue result = new org.tdmx.server.pcs.protobuf.Common.AttributeValue(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.tdmx.server.pcs.protobuf.Common.AttributeValue) {
          return mergeFrom((org.tdmx.server.pcs.protobuf.Common.AttributeValue)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.tdmx.server.pcs.protobuf.Common.AttributeValue other) {
        if (other == org.tdmx.server.pcs.protobuf.Common.AttributeValue.getDefaultInstance()) return this;
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasName()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.tdmx.server.pcs.protobuf.Common.AttributeValue parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.tdmx.server.pcs.protobuf.Common.AttributeValue) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId name_ = org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId.AccountZoneId;
      /**
       * <code>required .AttributeValue.AttributeId name = 1;</code>
       */
      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required .AttributeValue.AttributeId name = 1;</code>
       */
      public org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId getName() {
        return name_;
      }
      /**
       * <code>required .AttributeValue.AttributeId name = 1;</code>
       */
      public Builder setName(org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required .AttributeValue.AttributeId name = 1;</code>
       */
      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = org.tdmx.server.pcs.protobuf.Common.AttributeValue.AttributeId.AccountZoneId;
        onChanged();
        return this;
      }

      private long value_ ;
      /**
       * <code>optional int64 value = 2;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional int64 value = 2;</code>
       */
      public long getValue() {
        return value_;
      }
      /**
       * <code>optional int64 value = 2;</code>
       */
      public Builder setValue(long value) {
        bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int64 value = 2;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = 0L;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:AttributeValue)
    }

    static {
      defaultInstance = new AttributeValue(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:AttributeValue)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AttributeValue_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AttributeValue_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014common.proto\"\227\002\n\016AttributeValue\022)\n\004nam" +
      "e\030\001 \002(\0162\033.AttributeValue.AttributeId\022\r\n\005" +
      "value\030\002 \001(\003\"\312\001\n\013AttributeId\022\021\n\rAccountZo" +
      "neId\020\000\022\n\n\006ZoneId\020\001\022\014\n\010DomainId\020\002\022\r\n\tChan" +
      "nelId\020\003\022\026\n\022TemporaryChannelId\020\004\022\r\n\tServi" +
      "ceId\020\005\022\r\n\tAddressId\020\006\022\023\n\017AuthorizationId" +
      "\020\007\022\017\n\013FlowQuotaId\020\010\022\r\n\tMessageId\020\t\022\024\n\020De" +
      "liveryReportId\020\n*h\n\nObjectType\022\013\n\007Messag" +
      "e\020\000\022\021\n\rDeliveryReply\020\001\022\021\n\rAuthorization\020" +
      "\002\022\026\n\022DestinationSession\020\003\022\017\n\013FlowControl",
      "\020\004B&\n\034org.tdmx.server.pcs.protobufB\006Comm" +
      "on"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_AttributeValue_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_AttributeValue_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_AttributeValue_descriptor,
        new java.lang.String[] { "Name", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
