package org.moyowi.anacoluthe

package object glyphe {

  object Aristote{

  }

  object NamespaceElement {
    val FQN_SEPARATOR = "::" //$NON-NLS-1$
  }

  trait NamespaceElement {
    def namespace: Namespace

    def name: String

    def fullyQualifiedName: String
  }

  trait TypeTag {
    def asString: String

    def asByteArray: Array[Byte]
  }

  trait Type extends NamespaceElement {
    def getSuperTypes: Iterable[Type]

    def getAllSuperTypes: Iterable[Type]

    def getTag: TypeTag
  }

  trait ValueObject[T] extends Any {
    def getValue: T
  }

  trait ValueType[T] extends Type {
    def createValue(value: T): ValueObject[T]

    def getDefaultValue: ValueObject[T]
  }

  trait Namespace extends NamespaceElement {
    def getSubNamespaces: Iterable[Namespace]

    def getTypes: Iterable[Type]

    def getMetaElements: Iterable[MetaElement]

    def getValueTypes: Iterable[ValueType[_]]

    def add(namespace: Namespace): Unit

    def add(metaElement: MetaElement): Unit

    def add(valueType: ValueType[_]): Unit

    def remove(namespace: Namespace): Unit

    def remove(metaElement: MetaElement): Unit

    def remove(valueType: ValueType[_]): Unit

    def getSubNamespace(name: String): Namespace

    def getMetaElement(name: String): MetaElement

    def getValueType(name: String): ValueType[_]

    def getType(name: String): Type

    def getTypeTag(name: String): TypeTag
  }

  sealed trait Any{
    val `type` : Type
 //   val typeTag : TypeTag
  }

  trait Model {
    def getReferenceModel: ReferenceModel

    def conformsTo(referenceModels: ReferenceModel): Boolean

    def createModelElement(metaElement: MetaElement): ModelElement

    def getElementsByKind(metaElement: MetaElement): Set[ModelElement]

    def getContents: Iterable[ModelElement]
  }

  trait ReferenceModel {
    def getNamespaces: Iterable[Namespace]

    def addNamespace(namespace: Namespace): Unit

    def getImportedNamespaces: Set[Namespace]

    def addImportedNamespace(namespace: Namespace): Unit

     def getNamespace(name: String): Namespace

    def getMetaElement(name: String): MetaElement
  }

  trait ModelElement extends Any {
    def getModel: Model

     def getType: MetaElement

     def get(feature: Feature): Property

    def get(feature: String): Property

    def getAsMonoValued(feature: Feature): Property.MonoValued

    def getAsMonoValued(feature: String): Property.MonoValued

    def getAsMultiValued(feature: Feature): Property.MultiValued

    def getAsMultiValued(feature: String): Property.MultiValued

    def unset(feature: Feature): Unit

    def unset(feature: String): Unit

    def isSet(feature: Feature): Boolean

    def isSet(feature: String): Boolean

    def getImmediateComposite: ModelElement

    def isKindOf(reference: MetaElement): Boolean

    def getId: Long
  }

  trait MetaElement extends Type {
    def isAbstract: Boolean

    def getFeature(featureName: String): Feature

    def getFeatures: Iterable[Feature]

    def getAllFeatures: Iterable[Feature]

    def isA(reference: MetaElement): Boolean
  }

  object Property {

    trait MonoValued extends Property {
      override def getValue: Any

      def set(value: Any): Unit
    }

    object MultiValued {

      object Ordered {

        trait Unique extends MultiValued.Ordered {
          override def getValue: Collection[Any]
        }

        trait NonUnique extends MultiValued.Ordered {
          override def getValue: Collection[Any]
        }

      }

      trait Ordered extends Property.MultiValued {
        def set(index: Int, element: Any): Unit

        def add(index: Int, element: Any): Unit

        def move(newPosition: Int, `object`: Any): Unit

        def move(newPosition: Int, oldPosition: Int): Unit

        def remove(index: Int): Unit
      }

      object Unordered {

        trait Unique extends MultiValued.Unordered {
          override def getValue: Collection[Any]
        }

        trait NonUnique extends MultiValued.Unordered {
          def remove(element: Nothing, occurrences: Int): Unit

          def removeAllOccurrences(element: Nothing): Unit

          override def getValue: Nothing
        }

      }

      trait Unordered extends Property.MultiValued {}

    }

    trait MultiValued extends Property {
      override def getValue: Collection[Any]

      def add(value: Any): Unit

      def remove(value: Any): Unit
    }

  }

  trait Collection[T] extends Any {
    def add(e: T): Collection[T]

    def remove(e: T): Collection[T]

    def addAll(c: Collection[T]): Collection[T]

    def removeAll(c: Collection[T]): Collection[T]

    def retainAll(c: Collection[T]): Collection[T]

    def clear: Collection[T]
  }

  trait Property extends Any {
    def getValue: Any

    def getModelElement: ModelElement

    def getFeature: Feature
  }

  object Feature {

    object MultiValued {
      val UNBOUNDED_UPPER_BOUND: Int = -1
    }

    trait MultiValued extends Feature {
      def isUnique: Boolean

      def isOrdered: Boolean

      def getLowerBound: Int

      def getUpperBound: Int
    }

  }

  trait Feature {
    def getName: String

    def getOwner: MetaElement

    def hasOpposite: Boolean

    def getOpposite: Feature

    def isContainment: Boolean

    def isValueObject: Boolean

    def isOptional: Boolean

    def isMultiValued: Boolean

    def getType: Type
  }

}
