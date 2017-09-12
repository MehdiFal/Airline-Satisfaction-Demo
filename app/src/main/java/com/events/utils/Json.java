package com.events.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.events.model.Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Json {

    private final static ObjectMapper mapper = new ObjectMapper ()
        .setSerializationInclusion (Include.NON_NULL)
        .configure (JsonParser.Feature.ALLOW_COMMENTS, true);

    public static ObjectMapper getMapperInstance () {
        return mapper;
    }

    public static JsonNode load (InputStream is) throws IOException {
        return mapper.readTree (is);
    }

    public static ArrayNode createArray () {
        return mapper.createArrayNode ();
    }

    public static ObjectNode createObject () {
        return mapper.createObjectNode ();
    }

    public static JsonNode build (String input) {
        if (Lang.isNullOrEmpty (input)) {
            return null;
        }
        try {
            return mapper.readTree (input);
        } catch (IOException e) {
            Logger.error (Json.class.getSimpleName () + " --> build", e);
            return null;
        }
    }

    public static <T> T buildObjectFromJsonStr (String jsonStr, Class<T> modelClass) {
        try {
            return mapper.readValue (jsonStr, modelClass);
        } catch (IOException e) {
            Logger.error (Json.class.getSimpleName () + " --> buildObjectFromJsonStr", e);
            return null;
        }
    }
    
    public static <T> T buildObjectFromJson (JsonNode jsonObj, Class<T> modelClass) {
        try {
            return mapper.treeToValue (jsonObj, modelClass);
        } catch (IOException e) {
            Logger.error (Json.class.getSimpleName () + " --> buildObjectFromJson", e);
            return null;
        }
    }
	
	public static <T> List<T> buildListFromJson (JsonNode jsonObj, Class<T[]> modelClass) {
		try {
			return new ArrayList<T> (Arrays.asList (mapper.treeToValue (jsonObj, modelClass)));
		} catch (IOException e) {
            Logger.error (Json.class.getSimpleName () + " --> buildListFromJson", e);
            return null;
		}
	}

    public static <T> List<T> buildListFromJsonStr (String jsonStr, Class<T[]> modelClass) {
        try {
            return new ArrayList<T> (Arrays.asList (mapper.readValue (jsonStr, modelClass)));
        } catch (IOException e) {
            Logger.error (Json.class.getSimpleName () + " --> buildListFromJsonStr", e);
            return null;
        }
    }

    public static <T> JsonNode buildNodeFromList (List<T> input) {
		if (input == null || input.size () == 0) {
			return createArray ();
		}
	
		ArrayNode array = createArray ();
		for (T entry : input) {
			if (entry instanceof Model) {
				array.add (build (entry.toString ()));
				continue;
			}
			array.add (entry.toString ());
		}
	
		return array;
	}

    public static JsonNode find (String path, JsonNode inputNode) {
        String[] treeDepth = path.split ("\\.");

        if (!inputNode.has (path) && treeDepth.length == 0) {
            return null;
        }

        JsonNode node = inputNode;
        for (String depth : treeDepth) {
            if (depth.contains ("[") && depth.endsWith ("]")) {
                String[] split = depth.substring (0, depth.length () - 1).split ("\\[");
                node = node.get (split [0]);

                if (node != null && node.isArray ()) {
                    node = node.get (Integer.valueOf (split [1]));
                    continue;
                }

                return null;
            }

            if (!node.has (depth)) {
                return null;
            }

            node = node.get (depth);
        }

        return node;
    }

    public static void deepCreate (String parentTree, Object object, ObjectNode inputNode) {
        String[] treeDepth = parentTree.split ("\\.");

        if (!inputNode.has (parentTree) && treeDepth.length == 0) {
            if (object instanceof JsonNode) {
                inputNode.set (parentTree, (JsonNode)object);
                return;
            }
            inputNode.put (parentTree, object.toString ());
            return;
        }

        int length 		= treeDepth.length;
        ObjectNode node = inputNode;
        for (int i = 0; i < length; i ++) {
            String depth 			= treeDepth [i];
            JsonNode currentNode 	= node.get (depth);

            if (i < length - 1) {
                if (currentNode == null) {
                    node.set (depth, Json.createObject ());
                }

                try {
                    node = (ObjectNode)node.get (depth);
                } catch (ClassCastException e) {
                    throw new ClassCastException (
                            "\n"
                                    + "Trying to replace the field: '" + currentNode + "'\n"
                                    + "on: " + inputNode + "\n"
                                    + "from: " + currentNode.getClass ().getSimpleName () + ":" + currentNode + "\n"
                                    + "to Json."
                    );
                }
                continue;
            }

            if (currentNode != null) {
                boolean castExc = (object instanceof ObjectNode && !(currentNode instanceof ObjectNode))
                        || (object instanceof ArrayNode && !(currentNode instanceof ArrayNode))
                        || (!(object instanceof ObjectNode) && currentNode instanceof ObjectNode)
                        || (!(object instanceof ArrayNode) && currentNode instanceof ArrayNode);
                if (castExc) {
                    throw new ClassCastException (
                            "\n"
                                    + "Trying to replace the field: '" + depth + "'\n"
                                    + "on: " + inputNode + "\n"
                                    + "from: " + currentNode.getClass ().getSimpleName () + ":" + currentNode + "\n"
                                    + "to: " + object.getClass ().getSimpleName () + ":" + object
                    );
                }
            }

            if (object instanceof JsonNode) {
                node.set (depth, (JsonNode)object);
            } else {
                node.put (depth, object.toString ());
            }
        }
    }

    public static String toString (Object value) {
        try {
            return mapper.writeValueAsString (value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException (e.getMessage (), e);
        }
    }

	public static String buildPath (String... args) {
		if (args == null || args.length == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < args.length; i ++) {
			sb.append (args [i]);
			if (i != args.length - 1) {
				sb.append (Lang.DOT);
			}
		}

		return sb.toString ();
	}
}