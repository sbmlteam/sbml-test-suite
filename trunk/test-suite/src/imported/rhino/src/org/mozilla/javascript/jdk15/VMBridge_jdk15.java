/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-2000
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.mozilla.javascript.jdk15;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public class VMBridge_jdk15 extends org.mozilla.javascript.jdk13.VMBridge_jdk13
{
    public VMBridge_jdk15() throws SecurityException, InstantiationException {
        try {
            // Just try and see if we can access the isVarArgs method.
            // We want to fail loading if the method does not exist
            // so that we can load a bridge to an older JDK instead.
            Method.class.getMethod("isVarArgs", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // Throw a fittitng exception that is handled by
            // org.mozilla.javascript.Kit.newInstanceOrNull:
            throw new InstantiationException(e.getMessage());
        }
    }
    public boolean isVarArgs(Member member) {
        if (member instanceof Method)
            return ((Method) member).isVarArgs();
        else if (member instanceof Constructor)
            return ((Constructor) member).isVarArgs();
        else
            return false;
    }
}
