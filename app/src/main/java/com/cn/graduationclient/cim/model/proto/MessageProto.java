package com.cn.graduationclient.cim.model.proto;

public final class MessageProto {
  private MessageProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface ModelOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.farsunset.cim.sdk.android.model.proto.Model)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional int64 id = 1;</code>
     */
    long getId();

    /**
     * <code>optional string action = 2;</code>
     */
    String getAction();
    /**
     * <code>optional string action = 2;</code>
     */
    com.google.protobuf.ByteString
        getActionBytes();

    /**
     * <code>optional string content = 3;</code>
     */
    String getContent();
    /**
     * <code>optional string content = 3;</code>
     */
    com.google.protobuf.ByteString
        getContentBytes();

    /**
     * <code>optional string sender = 4;</code>
     */
    String getSender();
    /**
     * <code>optional string sender = 4;</code>
     */
    com.google.protobuf.ByteString
        getSenderBytes();

    /**
     * <code>optional string receiver = 5;</code>
     */
    String getReceiver();
    /**
     * <code>optional string receiver = 5;</code>
     */
    com.google.protobuf.ByteString
        getReceiverBytes();

    /**
     * <code>optional string extra = 6;</code>
     */
    String getExtra();
    /**
     * <code>optional string extra = 6;</code>
     */
    com.google.protobuf.ByteString
        getExtraBytes();

    /**
     * <code>optional string title = 7;</code>
     */
    String getTitle();
    /**
     * <code>optional string title = 7;</code>
     */
    com.google.protobuf.ByteString
        getTitleBytes();

    /**
     * <code>optional string format = 8;</code>
     */
    String getFormat();
    /**
     * <code>optional string format = 8;</code>
     */
    com.google.protobuf.ByteString
        getFormatBytes();

    /**
     * <code>optional int64 timestamp = 9;</code>
     */
    long getTimestamp();
  }
  /**
   * Protobuf type {@code com.farsunset.cim.sdk.android.model.proto.Model}
   */
  public  static final class Model extends
      com.google.protobuf.GeneratedMessageLite<
          Model, Model.Builder> implements
      // @@protoc_insertion_point(message_implements:com.farsunset.cim.sdk.android.model.proto.Model)
      ModelOrBuilder {
    private Model() {
      action_ = "";
      content_ = "";
      sender_ = "";
      receiver_ = "";
      extra_ = "";
      title_ = "";
      format_ = "";
    }
    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <code>optional int64 id = 1;</code>
     */
    public long getId() {
      return id_;
    }
    /**
     * <code>optional int64 id = 1;</code>
     */
    private void setId(long value) {
      
      id_ = value;
    }
    /**
     * <code>optional int64 id = 1;</code>
     */
    private void clearId() {
      
      id_ = 0L;
    }

    public static final int ACTION_FIELD_NUMBER = 2;
    private String action_;
    /**
     * <code>optional string action = 2;</code>
     */
    public String getAction() {
      return action_;
    }
    /**
     * <code>optional string action = 2;</code>
     */
    public com.google.protobuf.ByteString
        getActionBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(action_);
    }
    /**
     * <code>optional string action = 2;</code>
     */
    private void setAction(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      action_ = value;
    }
    /**
     * <code>optional string action = 2;</code>
     */
    private void clearAction() {
      
      action_ = getDefaultInstance().getAction();
    }
    /**
     * <code>optional string action = 2;</code>
     */
    private void setActionBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      action_ = value.toStringUtf8();
    }

    public static final int CONTENT_FIELD_NUMBER = 3;
    private String content_;
    /**
     * <code>optional string content = 3;</code>
     */
    public String getContent() {
      return content_;
    }
    /**
     * <code>optional string content = 3;</code>
     */
    public com.google.protobuf.ByteString
        getContentBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(content_);
    }
    /**
     * <code>optional string content = 3;</code>
     */
    private void setContent(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      content_ = value;
    }
    /**
     * <code>optional string content = 3;</code>
     */
    private void clearContent() {
      
      content_ = getDefaultInstance().getContent();
    }
    /**
     * <code>optional string content = 3;</code>
     */
    private void setContentBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      content_ = value.toStringUtf8();
    }

    public static final int SENDER_FIELD_NUMBER = 4;
    private String sender_;
    /**
     * <code>optional string sender = 4;</code>
     */
    public String getSender() {
      return sender_;
    }
    /**
     * <code>optional string sender = 4;</code>
     */
    public com.google.protobuf.ByteString
        getSenderBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(sender_);
    }
    /**
     * <code>optional string sender = 4;</code>
     */
    private void setSender(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      sender_ = value;
    }
    /**
     * <code>optional string sender = 4;</code>
     */
    private void clearSender() {
      
      sender_ = getDefaultInstance().getSender();
    }
    /**
     * <code>optional string sender = 4;</code>
     */
    private void setSenderBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      sender_ = value.toStringUtf8();
    }

    public static final int RECEIVER_FIELD_NUMBER = 5;
    private String receiver_;
    /**
     * <code>optional string receiver = 5;</code>
     */
    public String getReceiver() {
      return receiver_;
    }
    /**
     * <code>optional string receiver = 5;</code>
     */
    public com.google.protobuf.ByteString
        getReceiverBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(receiver_);
    }
    /**
     * <code>optional string receiver = 5;</code>
     */
    private void setReceiver(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      receiver_ = value;
    }
    /**
     * <code>optional string receiver = 5;</code>
     */
    private void clearReceiver() {
      
      receiver_ = getDefaultInstance().getReceiver();
    }
    /**
     * <code>optional string receiver = 5;</code>
     */
    private void setReceiverBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      receiver_ = value.toStringUtf8();
    }

    public static final int EXTRA_FIELD_NUMBER = 6;
    private String extra_;
    /**
     * <code>optional string extra = 6;</code>
     */
    public String getExtra() {
      return extra_;
    }
    /**
     * <code>optional string extra = 6;</code>
     */
    public com.google.protobuf.ByteString
        getExtraBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(extra_);
    }
    /**
     * <code>optional string extra = 6;</code>
     */
    private void setExtra(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      extra_ = value;
    }
    /**
     * <code>optional string extra = 6;</code>
     */
    private void clearExtra() {
      
      extra_ = getDefaultInstance().getExtra();
    }
    /**
     * <code>optional string extra = 6;</code>
     */
    private void setExtraBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      extra_ = value.toStringUtf8();
    }

    public static final int TITLE_FIELD_NUMBER = 7;
    private String title_;
    /**
     * <code>optional string title = 7;</code>
     */
    public String getTitle() {
      return title_;
    }
    /**
     * <code>optional string title = 7;</code>
     */
    public com.google.protobuf.ByteString
        getTitleBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(title_);
    }
    /**
     * <code>optional string title = 7;</code>
     */
    private void setTitle(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      title_ = value;
    }
    /**
     * <code>optional string title = 7;</code>
     */
    private void clearTitle() {
      
      title_ = getDefaultInstance().getTitle();
    }
    /**
     * <code>optional string title = 7;</code>
     */
    private void setTitleBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      title_ = value.toStringUtf8();
    }

    public static final int FORMAT_FIELD_NUMBER = 8;
    private String format_;
    /**
     * <code>optional string format = 8;</code>
     */
    public String getFormat() {
      return format_;
    }
    /**
     * <code>optional string format = 8;</code>
     */
    public com.google.protobuf.ByteString
        getFormatBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(format_);
    }
    /**
     * <code>optional string format = 8;</code>
     */
    private void setFormat(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      format_ = value;
    }
    /**
     * <code>optional string format = 8;</code>
     */
    private void clearFormat() {
      
      format_ = getDefaultInstance().getFormat();
    }
    /**
     * <code>optional string format = 8;</code>
     */
    private void setFormatBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      format_ = value.toStringUtf8();
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 9;
    private long timestamp_;
    /**
     * <code>optional int64 timestamp = 9;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>optional int64 timestamp = 9;</code>
     */
    private void setTimestamp(long value) {
      
      timestamp_ = value;
    }
    /**
     * <code>optional int64 timestamp = 9;</code>
     */
    private void clearTimestamp() {
      
      timestamp_ = 0L;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (!action_.isEmpty()) {
        output.writeString(2, getAction());
      }
      if (!content_.isEmpty()) {
        output.writeString(3, getContent());
      }
      if (!sender_.isEmpty()) {
        output.writeString(4, getSender());
      }
      if (!receiver_.isEmpty()) {
        output.writeString(5, getReceiver());
      }
      if (!extra_.isEmpty()) {
        output.writeString(6, getExtra());
      }
      if (!title_.isEmpty()) {
        output.writeString(7, getTitle());
      }
      if (!format_.isEmpty()) {
        output.writeString(8, getFormat());
      }
      if (timestamp_ != 0L) {
        output.writeInt64(9, timestamp_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (!action_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getAction());
      }
      if (!content_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getContent());
      }
      if (!sender_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getSender());
      }
      if (!receiver_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(5, getReceiver());
      }
      if (!extra_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(6, getExtra());
      }
      if (!title_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(7, getTitle());
      }
      if (!format_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(8, getFormat());
      }
      if (timestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(9, timestamp_);
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static Model parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static Model parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static Model parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static Model parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static Model parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static Model parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static Model parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static Model parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static Model parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Model prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * Protobuf type {@code com.farsunset.cim.sdk.android.model.proto.Model}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          Model, Builder> implements
        // @@protoc_insertion_point(builder_implements:com.farsunset.cim.sdk.android.model.proto.Model)
        ModelOrBuilder {
      // Construct using com.farsunset.cim.sdk.android.model.proto.MessageProto.Model.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>optional int64 id = 1;</code>
       */
      public long getId() {
        return instance.getId();
      }
      /**
       * <code>optional int64 id = 1;</code>
       */
      public Builder setId(long value) {
        copyOnWrite();
        instance.setId(value);
        return this;
      }
      /**
       * <code>optional int64 id = 1;</code>
       */
      public Builder clearId() {
        copyOnWrite();
        instance.clearId();
        return this;
      }

      /**
       * <code>optional string action = 2;</code>
       */
      public String getAction() {
        return instance.getAction();
      }
      /**
       * <code>optional string action = 2;</code>
       */
      public com.google.protobuf.ByteString
          getActionBytes() {
        return instance.getActionBytes();
      }
      /**
       * <code>optional string action = 2;</code>
       */
      public Builder setAction(
          String value) {
        copyOnWrite();
        instance.setAction(value);
        return this;
      }
      /**
       * <code>optional string action = 2;</code>
       */
      public Builder clearAction() {
        copyOnWrite();
        instance.clearAction();
        return this;
      }
      /**
       * <code>optional string action = 2;</code>
       */
      public Builder setActionBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setActionBytes(value);
        return this;
      }

      /**
       * <code>optional string content = 3;</code>
       */
      public String getContent() {
        return instance.getContent();
      }
      /**
       * <code>optional string content = 3;</code>
       */
      public com.google.protobuf.ByteString
          getContentBytes() {
        return instance.getContentBytes();
      }
      /**
       * <code>optional string content = 3;</code>
       */
      public Builder setContent(
          String value) {
        copyOnWrite();
        instance.setContent(value);
        return this;
      }
      /**
       * <code>optional string content = 3;</code>
       */
      public Builder clearContent() {
        copyOnWrite();
        instance.clearContent();
        return this;
      }
      /**
       * <code>optional string content = 3;</code>
       */
      public Builder setContentBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setContentBytes(value);
        return this;
      }

      /**
       * <code>optional string sender = 4;</code>
       */
      public String getSender() {
        return instance.getSender();
      }
      /**
       * <code>optional string sender = 4;</code>
       */
      public com.google.protobuf.ByteString
          getSenderBytes() {
        return instance.getSenderBytes();
      }
      /**
       * <code>optional string sender = 4;</code>
       */
      public Builder setSender(
          String value) {
        copyOnWrite();
        instance.setSender(value);
        return this;
      }
      /**
       * <code>optional string sender = 4;</code>
       */
      public Builder clearSender() {
        copyOnWrite();
        instance.clearSender();
        return this;
      }
      /**
       * <code>optional string sender = 4;</code>
       */
      public Builder setSenderBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setSenderBytes(value);
        return this;
      }

      /**
       * <code>optional string receiver = 5;</code>
       */
      public String getReceiver() {
        return instance.getReceiver();
      }
      /**
       * <code>optional string receiver = 5;</code>
       */
      public com.google.protobuf.ByteString
          getReceiverBytes() {
        return instance.getReceiverBytes();
      }
      /**
       * <code>optional string receiver = 5;</code>
       */
      public Builder setReceiver(
          String value) {
        copyOnWrite();
        instance.setReceiver(value);
        return this;
      }
      /**
       * <code>optional string receiver = 5;</code>
       */
      public Builder clearReceiver() {
        copyOnWrite();
        instance.clearReceiver();
        return this;
      }
      /**
       * <code>optional string receiver = 5;</code>
       */
      public Builder setReceiverBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setReceiverBytes(value);
        return this;
      }

      /**
       * <code>optional string extra = 6;</code>
       */
      public String getExtra() {
        return instance.getExtra();
      }
      /**
       * <code>optional string extra = 6;</code>
       */
      public com.google.protobuf.ByteString
          getExtraBytes() {
        return instance.getExtraBytes();
      }
      /**
       * <code>optional string extra = 6;</code>
       */
      public Builder setExtra(
          String value) {
        copyOnWrite();
        instance.setExtra(value);
        return this;
      }
      /**
       * <code>optional string extra = 6;</code>
       */
      public Builder clearExtra() {
        copyOnWrite();
        instance.clearExtra();
        return this;
      }
      /**
       * <code>optional string extra = 6;</code>
       */
      public Builder setExtraBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setExtraBytes(value);
        return this;
      }

      /**
       * <code>optional string title = 7;</code>
       */
      public String getTitle() {
        return instance.getTitle();
      }
      /**
       * <code>optional string title = 7;</code>
       */
      public com.google.protobuf.ByteString
          getTitleBytes() {
        return instance.getTitleBytes();
      }
      /**
       * <code>optional string title = 7;</code>
       */
      public Builder setTitle(
          String value) {
        copyOnWrite();
        instance.setTitle(value);
        return this;
      }
      /**
       * <code>optional string title = 7;</code>
       */
      public Builder clearTitle() {
        copyOnWrite();
        instance.clearTitle();
        return this;
      }
      /**
       * <code>optional string title = 7;</code>
       */
      public Builder setTitleBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setTitleBytes(value);
        return this;
      }

      /**
       * <code>optional string format = 8;</code>
       */
      public String getFormat() {
        return instance.getFormat();
      }
      /**
       * <code>optional string format = 8;</code>
       */
      public com.google.protobuf.ByteString
          getFormatBytes() {
        return instance.getFormatBytes();
      }
      /**
       * <code>optional string format = 8;</code>
       */
      public Builder setFormat(
          String value) {
        copyOnWrite();
        instance.setFormat(value);
        return this;
      }
      /**
       * <code>optional string format = 8;</code>
       */
      public Builder clearFormat() {
        copyOnWrite();
        instance.clearFormat();
        return this;
      }
      /**
       * <code>optional string format = 8;</code>
       */
      public Builder setFormatBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setFormatBytes(value);
        return this;
      }

      /**
       * <code>optional int64 timestamp = 9;</code>
       */
      public long getTimestamp() {
        return instance.getTimestamp();
      }
      /**
       * <code>optional int64 timestamp = 9;</code>
       */
      public Builder setTimestamp(long value) {
        copyOnWrite();
        instance.setTimestamp(value);
        return this;
      }
      /**
       * <code>optional int64 timestamp = 9;</code>
       */
      public Builder clearTimestamp() {
        copyOnWrite();
        instance.clearTimestamp();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.farsunset.cim.sdk.android.model.proto.Model)
    }
    protected final Object dynamicMethod(
        MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new Model();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          Model other = (Model) arg1;
          id_ = visitor.visitLong(id_ != 0L, id_,
              other.id_ != 0L, other.id_);
          action_ = visitor.visitString(!action_.isEmpty(), action_,
              !other.action_.isEmpty(), other.action_);
          content_ = visitor.visitString(!content_.isEmpty(), content_,
              !other.content_.isEmpty(), other.content_);
          sender_ = visitor.visitString(!sender_.isEmpty(), sender_,
              !other.sender_.isEmpty(), other.sender_);
          receiver_ = visitor.visitString(!receiver_.isEmpty(), receiver_,
              !other.receiver_.isEmpty(), other.receiver_);
          extra_ = visitor.visitString(!extra_.isEmpty(), extra_,
              !other.extra_.isEmpty(), other.extra_);
          title_ = visitor.visitString(!title_.isEmpty(), title_,
              !other.title_.isEmpty(), other.title_);
          format_ = visitor.visitString(!format_.isEmpty(), format_,
              !other.format_.isEmpty(), other.format_);
          timestamp_ = visitor.visitLong(timestamp_ != 0L, timestamp_,
              other.timestamp_ != 0L, other.timestamp_);
          if (visitor == MergeFromVisitor
              .INSTANCE) {
          }
          return this;
        }
        case MERGE_FROM_STREAM: {
          com.google.protobuf.CodedInputStream input =
              (com.google.protobuf.CodedInputStream) arg0;
          com.google.protobuf.ExtensionRegistryLite extensionRegistry =
              (com.google.protobuf.ExtensionRegistryLite) arg1;
          try {
            boolean done = false;
            while (!done) {
              int tag = input.readTag();
              switch (tag) {
                case 0:
                  done = true;
                  break;
                default: {
                  if (!input.skipField(tag)) {
                    done = true;
                  }
                  break;
                }
                case 8: {

                  id_ = input.readInt64();
                  break;
                }
                case 18: {
                  String s = input.readStringRequireUtf8();

                  action_ = s;
                  break;
                }
                case 26: {
                  String s = input.readStringRequireUtf8();

                  content_ = s;
                  break;
                }
                case 34: {
                  String s = input.readStringRequireUtf8();

                  sender_ = s;
                  break;
                }
                case 42: {
                  String s = input.readStringRequireUtf8();

                  receiver_ = s;
                  break;
                }
                case 50: {
                  String s = input.readStringRequireUtf8();

                  extra_ = s;
                  break;
                }
                case 58: {
                  String s = input.readStringRequireUtf8();

                  title_ = s;
                  break;
                }
                case 66: {
                  String s = input.readStringRequireUtf8();

                  format_ = s;
                  break;
                }
                case 72: {

                  timestamp_ = input.readInt64();
                  break;
                }
              }
            }
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException(e.setUnfinishedMessage(this));
          } catch (java.io.IOException e) {
            throw new RuntimeException(
                new com.google.protobuf.InvalidProtocolBufferException(
                    e.getMessage()).setUnfinishedMessage(this));
          } finally {
          }
        }
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          if (PARSER == null) {    synchronized (Model.class) {
              if (PARSER == null) {
                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
              }
            }
          }
          return PARSER;
        }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:com.farsunset.cim.sdk.android.model.proto.Model)
    private static final Model DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Model();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static Model getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<Model> PARSER;

    public static com.google.protobuf.Parser<Model> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
