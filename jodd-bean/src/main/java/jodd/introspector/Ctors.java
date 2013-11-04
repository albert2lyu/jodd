// Copyright (c) 2003-2013, Jodd Team (jodd.org). All Rights Reserved.

package jodd.introspector;

import java.lang.reflect.Constructor;

/**
 * Constructors collection.
 */
public class Ctors {

	protected final ClassDescriptor classDescriptor;
	protected final CtorDescriptor[] allCtors;
	protected CtorDescriptor defaultCtor;

	public Ctors(ClassDescriptor classDescriptor) {
		this.classDescriptor = classDescriptor;

		this.allCtors = inspectConstructors();
	}

	/**
	 * Add all ctors at once.
	 */
	protected CtorDescriptor[] inspectConstructors() {
		Class type = classDescriptor.getType();
		Constructor[] ctors = type.getDeclaredConstructors();

		CtorDescriptor[] allCtors = new CtorDescriptor[ctors.length];

		for (int i = 0; i < ctors.length; i++) {
			Constructor ctor = ctors[i];

			CtorDescriptor ctorDescriptor = createCtorDescriptor(ctor);
			allCtors[i] = ctorDescriptor;

			if (ctorDescriptor.isDefault()) {
				defaultCtor = ctorDescriptor;
			}
		}

		return allCtors;
	}

	protected CtorDescriptor createCtorDescriptor(Constructor ctor) {
		return new CtorDescriptor(classDescriptor, ctor);
	}

	// ---------------------------------------------------------------- get

	/**
	 * Returns default (no-args) ctor.
	 */
	public CtorDescriptor getDefaultCtor() {
		return defaultCtor;
	}

	/**
	 * Returns ctor for given argument types.
	 */
	public CtorDescriptor getCtorDescriptor(Class... args) {
		ctors:
		for (CtorDescriptor ctorDescriptor : allCtors) {
			Class[] arg = ctorDescriptor.getParameters();

			if (arg.length != args.length) {
				continue;
			}

			for (int j = 0; j < arg.length; j++) {
				if (arg[j] != args[j]) {
					continue ctors;
				}
			}

			return ctorDescriptor;
		}
		return null;
	}

	/**
	 * Returns all constructor descriptors.
	 */
	CtorDescriptor[] getAllCtorDescriptors() {
		return allCtors;
	}

}