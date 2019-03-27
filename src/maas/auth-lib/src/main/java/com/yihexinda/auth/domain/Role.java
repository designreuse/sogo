package com.yihexinda.auth.domain;

import java.util.List;

import com.yihexinda.core.abst.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Getter
@Setter
@Deprecated
public class Role extends AbstractEntity {
    private static final long serialVersionUID = -7771388894508968435L;
    String id;
    String name;
    List<Authority> authorities;
}
