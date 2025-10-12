package net.vercte.slabstack.platform;

import net.vercte.slabstack.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {
//    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> service) {
        return ServiceLoader.load(service)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + service.getName()));
    }
}
